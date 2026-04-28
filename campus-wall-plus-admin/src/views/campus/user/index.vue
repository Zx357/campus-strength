<template>
  <div class="p-16px">
    <ElCard class="mb-16px">
      <ElForm :model="query" inline>
        <ElFormItem label="关键词">
          <ElInput v-model="query.keyword" clearable placeholder="用户名/昵称/手机号" @keyup.enter="fetchData" />
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
          <span>用户管理</span>
          <ElButton type="primary" @click="openCreate">新增用户</ElButton>
        </div>
      </template>
      <ElTable v-loading="loading" :data="records" row-key="id" border>
        <ElTableColumn prop="username" label="用户名" width="150" />
        <ElTableColumn prop="nickname" label="昵称" width="150" />
        <ElTableColumn prop="phone" label="手机号" width="140" />
        <ElTableColumn prop="college" label="学院" width="160" />
        <ElTableColumn prop="roleCode" label="角色" width="150" />
        <ElTableColumn prop="authStatus" label="认证" width="110">
          <template #default="{ row }">
            <ElTag :type="row.authStatus === 2 ? 'success' : row.authStatus === 3 ? 'danger' : 'warning'">
              {{ authText(row.authStatus) }}
            </ElTag>
          </template>
        </ElTableColumn>
        <ElTableColumn prop="status" label="状态" width="100">
          <template #default="{ row }">
            <ElTag :type="row.status === 1 ? 'success' : 'danger'">{{ row.status === 1 ? '正常' : '禁用' }}</ElTag>
          </template>
        </ElTableColumn>
        <ElTableColumn label="操作" fixed="right" width="310">
          <template #default="{ row }">
            <ElButton link type="primary" @click="view(row)">详情</ElButton>
            <ElButton link type="primary" @click="openEdit(row)">编辑</ElButton>
            <ElButton link :type="row.status === 1 ? 'danger' : 'success'" @click="toggleBan(row)">
              {{ row.status === 1 ? '封禁' : '解封' }}
            </ElButton>
            <ElButton link type="success" @click="setAuth(row, 2)">认证通过</ElButton>
            <ElButton link type="warning" @click="setAuth(row, 3)">驳回</ElButton>
            <ElDropdown @command="role => setRole(row, String(role))">
              <ElButton link type="primary">角色</ElButton>
              <template #dropdown>
                <ElDropdownMenu>
                  <ElDropdownItem command="TENANT_ADMIN">学校管理员</ElDropdownItem>
                  <ElDropdownItem command="TENANT_AUDITOR">审核员</ElDropdownItem>
                  <ElDropdownItem command="STUDENT">学生</ElDropdownItem>
                </ElDropdownMenu>
              </template>
            </ElDropdown>
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

    <ElDialog v-model="formVisible" :title="form.id ? '编辑用户' : '新增用户'" width="560px">
      <ElForm ref="formRef" :model="form" label-width="100px">
        <ElFormItem label="用户名" prop="username" :rules="[{ required: true, message: '请输入用户名', trigger: 'blur' }]">
          <ElInput v-model="form.username" />
        </ElFormItem>
        <ElFormItem label="昵称">
          <ElInput v-model="form.nickname" />
        </ElFormItem>
        <ElFormItem label="手机号">
          <ElInput v-model="form.phone" />
        </ElFormItem>
        <ElFormItem label="学院">
          <ElInput v-model="form.college" />
        </ElFormItem>
        <ElFormItem label="专业">
          <ElInput v-model="form.major" />
        </ElFormItem>
        <ElFormItem label="角色">
          <ElSelect v-model="form.roleCode">
            <ElOption label="学校管理员" value="TENANT_ADMIN" />
            <ElOption label="审核员" value="TENANT_AUDITOR" />
            <ElOption label="学生" value="STUDENT" />
          </ElSelect>
        </ElFormItem>
      </ElForm>
      <template #footer>
        <ElButton @click="formVisible = false">取消</ElButton>
        <ElButton type="primary" @click="submit">保存</ElButton>
      </template>
    </ElDialog>

    <ElDrawer v-model="detailVisible" title="用户详情" size="420px">
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

const formRef = ref<FormInstance>();
const loading = ref(false);
const records = ref<any[]>([]);
const total = ref(0);
const query = reactive({ current: 1, size: 10, keyword: '' });
const formVisible = ref(false);
const detailVisible = ref(false);
const form = reactive<Record<string, any>>({ roleCode: 'STUDENT', status: 1, authStatus: 0 });
const detail = ref<Record<string, any>>({});

function authText(status: number) {
  return status === 2 ? '已认证' : status === 1 ? '待审核' : status === 3 ? '已驳回' : '未认证';
}

async function fetchData() {
  loading.value = true;
  try {
    const data: any = await campusApi.page('/api/admin/user', query);
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
  Object.assign(form, { roleCode: 'STUDENT', status: 1, authStatus: 0 });
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
  if (form.id) await campusApi.update('/api/admin/user', form);
  else await campusApi.create('/api/admin/user', form);
  ElMessage.success('保存成功');
  formVisible.value = false;
  fetchData();
}

async function toggleBan(row: any) {
  const next = row.status === 1 ? 0 : 1;
  await ElMessageBox.confirm(`确认${next === 1 ? '解封' : '封禁'}该用户吗？`, '提示', { type: 'warning' });
  await campusApi.userBan(row.id, next);
  ElMessage.success('用户状态已更新');
  fetchData();
}

async function setAuth(row: any, authStatus: number) {
  await campusApi.userAuth(row.id, authStatus);
  ElMessage.success(authStatus === 2 ? '认证已通过' : '认证已驳回');
  fetchData();
}

async function setRole(row: any, roleCode: string) {
  await campusApi.userRole(row.id, roleCode);
  ElMessage.success('角色已更新');
  fetchData();
}

onMounted(fetchData);
</script>
