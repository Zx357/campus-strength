<template>
  <div class="p-16px">
    <ElCard class="mb-16px">
      <ElForm :model="query" inline>
        <ElFormItem label="关键词">
          <ElInput v-model="query.keyword" clearable placeholder="举报原因/内容" @keyup.enter="fetchData" />
        </ElFormItem>
        <ElFormItem>
          <ElButton type="primary" @click="fetchData">搜索</ElButton>
          <ElButton @click="reset">重置</ElButton>
        </ElFormItem>
      </ElForm>
    </ElCard>

    <ElCard>
      <template #header>举报管理</template>
      <ElTable v-loading="loading" :data="records" row-key="id" border>
        <ElTableColumn prop="targetType" label="举报对象" width="130" />
        <ElTableColumn prop="targetId" label="对象ID" width="110" />
        <ElTableColumn prop="reasonType" label="原因类型" width="140" />
        <ElTableColumn prop="reasonContent" label="原因说明" show-overflow-tooltip />
        <ElTableColumn prop="status" label="状态" width="110">
          <template #default="{ row }">
            <ElTag :type="row.status === 1 ? 'success' : row.status === 2 ? 'info' : 'warning'">
              {{ row.status === 1 ? '已处理' : row.status === 2 ? '已驳回' : '待处理' }}
            </ElTag>
          </template>
        </ElTableColumn>
        <ElTableColumn prop="createdAt" label="举报时间" width="180" />
        <ElTableColumn label="操作" fixed="right" width="170">
          <template #default="{ row }">
            <ElButton link type="primary" @click="openHandle(row)">处理</ElButton>
            <ElButton link type="primary" @click="view(row)">详情</ElButton>
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

    <ElDialog v-model="handleVisible" title="处理举报" width="520px">
      <ElForm :model="handleForm" label-width="100px">
        <ElFormItem label="处理状态">
          <ElRadioGroup v-model="handleForm.status">
            <ElRadio :value="1">已处理</ElRadio>
            <ElRadio :value="2">驳回举报</ElRadio>
          </ElRadioGroup>
        </ElFormItem>
        <ElFormItem label="处理结果">
          <ElInput v-model="handleForm.handleResult" type="textarea" :rows="4" placeholder="请输入处理结果" />
        </ElFormItem>
        <ElFormItem label="下架内容">
          <ElSwitch v-model="handleForm.takeDown" />
        </ElFormItem>
      </ElForm>
      <template #footer>
        <ElButton @click="handleVisible = false">取消</ElButton>
        <ElButton type="primary" @click="submitHandle">提交处理</ElButton>
      </template>
    </ElDialog>

    <ElDrawer v-model="detailVisible" title="举报详情" size="420px">
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
import { ElMessage } from 'element-plus';
import { campusApi } from '@/service/api/campus';

const loading = ref(false);
const records = ref<any[]>([]);
const total = ref(0);
const query = reactive({ current: 1, size: 10, keyword: '' });
const handleVisible = ref(false);
const detailVisible = ref(false);
const detail = ref<Record<string, any>>({});
const handleForm = reactive({ id: 0, status: 1, handleResult: '', takeDown: false });

async function fetchData() {
  loading.value = true;
  try {
    const data: any = await campusApi.page('/api/admin/report', query);
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

function view(row: any) {
  detail.value = row;
  detailVisible.value = true;
}

function openHandle(row: any) {
  Object.assign(handleForm, {
    id: row.id,
    status: row.status === 2 ? 2 : 1,
    handleResult: row.handleResult || '',
    takeDown: false
  });
  handleVisible.value = true;
}

async function submitHandle() {
  if (!handleForm.handleResult) {
    ElMessage.warning('请填写处理结果');
    return;
  }
  await campusApi.handleReport({ ...handleForm });
  ElMessage.success('举报已处理');
  handleVisible.value = false;
  fetchData();
}

onMounted(fetchData);
</script>
