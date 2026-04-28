<template>
  <div>
    <CrudPage title="活动社团" base-url="/api/admin/activity" audit-type="activity" :columns="columns" :form-fields="formFields" />

    <div class="p-16px pt-0">
      <ElCard>
        <template #header>
          <div class="flex items-center justify-between">
            <span>活动报名名单</span>
            <ElButton type="primary" @click="fetchSignup">刷新报名</ElButton>
          </div>
        </template>
        <ElForm :model="signupQuery" inline>
          <ElFormItem label="活动ID">
            <ElInputNumber v-model="signupQuery.activityId" :min="1" clearable />
          </ElFormItem>
          <ElFormItem>
            <ElButton type="primary" @click="fetchSignup">查询</ElButton>
          </ElFormItem>
        </ElForm>
        <ElTable v-loading="signupLoading" :data="signupRecords" row-key="id" border>
          <ElTableColumn prop="activityId" label="活动ID" width="120" />
          <ElTableColumn prop="userId" label="用户ID" width="120" />
          <ElTableColumn prop="contactPhone" label="联系电话" width="150" />
          <ElTableColumn prop="remark" label="备注" show-overflow-tooltip />
          <ElTableColumn prop="status" label="状态" width="100">
            <template #default="{ row }">
              <ElTag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '有效' : '已取消' }}</ElTag>
            </template>
          </ElTableColumn>
          <ElTableColumn prop="createdAt" label="报名时间" width="180" />
        </ElTable>
        <div class="mt-16px flex justify-end">
          <ElPagination
            v-model:current-page="signupQuery.current"
            v-model:page-size="signupQuery.size"
            layout="total, sizes, prev, pager, next"
            :total="signupTotal"
            @change="fetchSignup"
          />
        </div>
      </ElCard>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import CrudPage from '../../components/crud-page.vue';
import { campusApi } from '@/service/api/campus';

const columns = [
  { prop: 'title', label: '标题' },
  { prop: 'activityTime', label: '活动时间' },
  { prop: 'activityLocation', label: '活动地点' },
  { prop: 'organizer', label: '组织方' }
];

const formFields = [
  { prop: 'title', label: '标题', required: true, type: 'text' },
  { prop: 'content', label: '内容', required: true, type: 'textarea' },
  { prop: 'activityLocation', label: '活动地点', required: false, type: 'text' },
  { prop: 'organizer', label: '组织方', required: false, type: 'text' }
];

const signupLoading = ref(false);
const signupRecords = ref<any[]>([]);
const signupTotal = ref(0);
const signupQuery = reactive<{ current: number; size: number; activityId?: number }>({ current: 1, size: 10 });

async function fetchSignup() {
  signupLoading.value = true;
  try {
    const data: any = await campusApi.activitySignupPage(signupQuery);
    signupRecords.value = data?.records || [];
    signupTotal.value = data?.total || 0;
  } finally {
    signupLoading.value = false;
  }
}

onMounted(fetchSignup);
</script>
