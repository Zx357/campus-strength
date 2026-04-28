<template>
  <div class="p-16px">
    <ElCard class="mb-16px">
      <ElForm :model="query" inline>
        <ElFormItem label="关键词">
          <ElInput v-model="query.keyword" clearable placeholder="请输入关键词" @keyup.enter="fetchData" />
        </ElFormItem>
        <ElFormItem>
          <ElButton type="primary" @click="fetchData">搜索</ElButton>
          <ElButton @click="reset">重置</ElButton>
        </ElFormItem>
      </ElForm>
    </ElCard>

    <ElCard>
      <template #header>
        <div class="flex items-center justify-between">
          <span>{{ title }}</span>
          <ElButton type="primary" @click="openCreate">新增</ElButton>
        </div>
      </template>

      <ElTable v-loading="loading" :data="records" row-key="id" border>
        <ElTableColumn
          v-for="col in columns"
          :key="col.prop"
          :prop="col.prop"
          :label="col.label"
          min-width="120"
          show-overflow-tooltip
        />
        <ElTableColumn label="状态" width="110">
          <template #default="{ row }">
            <ElTag v-if="Object.prototype.hasOwnProperty.call(row, 'status')" :type="statusType(row.status)">
              {{ statusText(row.status) }}
            </ElTag>
            <span v-else>-</span>
          </template>
        </ElTableColumn>
        <ElTableColumn label="操作" fixed="right" :width="auditType ? 360 : 220">
          <template #default="{ row }">
            <ElButton link type="primary" @click="view(row)">详情</ElButton>
            <ElButton link type="primary" @click="openEdit(row)">编辑</ElButton>
            <ElButton v-if="auditType" link type="success" @click="audit(row, true)">通过</ElButton>
            <ElButton v-if="auditType" link type="warning" @click="audit(row, false)">驳回</ElButton>
            <ElButton v-if="auditType" link type="danger" @click="takeDown(row)">下架</ElButton>
            <ElButton link type="danger" @click="remove(row)">删除</ElButton>
          </template>
        </ElTableColumn>
      </ElTable>

      <div class="mt-16px flex justify-end">
        <ElPagination
          v-model:current-page="query.current"
          v-model:page-size="query.size"
          layout="total, sizes, prev, pager, next"
          :total="total"
          @change="fetchData"
        />
      </div>
    </ElCard>

    <ElDialog v-model="formVisible" :title="form.id ? '编辑' : '新增'" width="560px">
      <ElForm ref="formRef" :model="form" label-width="110px">
        <ElFormItem
          v-for="field in formFields"
          :key="field.prop"
          :label="field.label"
          :prop="field.prop"
          :rules="field.required ? [{ required: true, message: `请输入${field.label}`, trigger: 'blur' }] : []"
        >
          <ElInput v-model="form[field.prop]" :type="field.type || 'text'" :placeholder="`请输入${field.label}`" />
        </ElFormItem>
      </ElForm>
      <template #footer>
        <ElButton @click="formVisible = false">取消</ElButton>
        <ElButton type="primary" @click="submit">提交</ElButton>
      </template>
    </ElDialog>

    <ElDrawer v-model="detailVisible" title="详情" size="420px">
      <ElDescriptions :column="1" border>
        <ElDescriptionsItem v-for="(value, key) in detail" :key="key" :label="String(key)">
          {{ value }}
        </ElDescriptionsItem>
      </ElDescriptions>
    </ElDrawer>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import type { FormInstance } from 'element-plus';
import { ElMessage, ElMessageBox } from 'element-plus';
import { campusApi } from '@/service/api/campus';

const props = defineProps<{ title: string; baseUrl: string; columns: any[]; formFields: any[]; auditType?: string }>();
const formRef = ref<FormInstance>();
const loading = ref(false);
const records = ref<any[]>([]);
const total = ref(0);
const query = reactive({ current: 1, size: 10, keyword: '' });
const formVisible = ref(false);
const detailVisible = ref(false);
const form = reactive<Record<string, any>>({});
const detail = ref<Record<string, any>>({});
const auditType = props.auditType;

function statusText(status: number) {
  const map: Record<number, string> = { 0: '待处理', 1: '正常', 2: '已完成', 3: '已删除', 4: '已驳回' };
  return map[status] || String(status);
}

function statusType(status: number) {
  if (status === 1) return 'success';
  if (status === 2) return 'info';
  if (status === 3 || status === 4) return 'danger';
  return 'warning';
}

async function fetchData() {
  loading.value = true;
  try {
    const data: any = await campusApi.page(props.baseUrl, query);
    records.value = data?.records || [];
    total.value = data?.total || 0;
  } finally {
    loading.value = false;
  }
}

function reset() {
  query.keyword = '';
  query.current = 1;
  fetchData();
}

function openCreate() {
  Object.keys(form).forEach(k => delete form[k]);
  formVisible.value = true;
}

function openEdit(row: any) {
  Object.keys(form).forEach(k => delete form[k]);
  Object.assign(form, row);
  formVisible.value = true;
}

function view(row: any) {
  detail.value = row;
  detailVisible.value = true;
}

async function submit() {
  await formRef.value?.validate();
  if (form.id) await campusApi.update(props.baseUrl, form);
  else await campusApi.create(props.baseUrl, form);
  ElMessage.success('操作成功');
  formVisible.value = false;
  fetchData();
}

async function remove(row: any) {
  await ElMessageBox.confirm('确认删除该数据吗？', '提示', { type: 'warning' });
  await campusApi.remove(props.baseUrl, row.id);
  ElMessage.success('删除成功');
  fetchData();
}

async function audit(row: any, pass: boolean) {
  let reason = '';
  if (pass) {
    await ElMessageBox.confirm('确认审核通过该内容吗？', '提示', { type: 'warning' });
  } else {
    const result = await ElMessageBox.prompt('请输入驳回原因', '审核驳回', {
      confirmButtonText: '确认驳回',
      cancelButtonText: '取消',
      inputType: 'textarea',
      inputValidator: value => Boolean(value && value.trim()),
      inputErrorMessage: '请填写驳回原因'
    });
    reason = result.value;
  }
  const payload = { targetType: auditType, targetId: row.id, reason };
  if (pass) await campusApi.auditPass(payload);
  else await campusApi.auditReject(payload);
  ElMessage.success(pass ? '审核已通过' : '内容已驳回');
  fetchData();
}

async function takeDown(row: any) {
  const statusMap: Record<string, number> = {
    post: 4,
    comment: 3,
    lost: 3,
    goods: 3,
    help: 3,
    activity: 2
  };
  await ElMessageBox.confirm('确认下架该内容吗？', '提示', { type: 'warning' });
  await campusApi.status(props.baseUrl, row.id, statusMap[auditType || ''] ?? 0);
  ElMessage.success('内容已下架');
  fetchData();
}

onMounted(fetchData);
</script>
