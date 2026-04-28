<template>
  <div class="p-16px">
    <ElCard class="mb-16px">
      <template #header>
        <div class="flex items-center justify-between">
          <span>审核中心</span>
          <ElButton type="primary" @click="fetchData">刷新</ElButton>
        </div>
      </template>

      <ElForm :model="auditForm" inline>
        <ElFormItem label="内容类型">
          <ElSelect v-model="auditForm.targetType" class="w-160px">
            <ElOption label="校园墙帖子" value="post" />
            <ElOption label="评论" value="comment" />
            <ElOption label="失物招领" value="lost" />
            <ElOption label="二手闲置" value="goods" />
            <ElOption label="校园互助" value="help" />
            <ElOption label="活动社团" value="activity" />
          </ElSelect>
        </ElFormItem>
        <ElFormItem label="内容ID">
          <ElInputNumber v-model="auditForm.targetId" :min="1" />
        </ElFormItem>
        <ElFormItem label="审核意见">
          <ElInput v-model="auditForm.reason" class="w-280px" clearable placeholder="通过可不填，驳回建议填写原因" />
        </ElFormItem>
        <ElFormItem>
          <ElButton type="success" @click="submitAudit(true)">审核通过</ElButton>
          <ElButton type="danger" @click="submitAudit(false)">审核驳回</ElButton>
        </ElFormItem>
      </ElForm>
    </ElCard>

    <ElCard>
      <template #header>审核记录</template>
      <ElTable v-loading="loading" :data="records" border row-key="id">
        <ElTableColumn prop="targetType" label="类型" width="130" />
        <ElTableColumn prop="targetId" label="内容ID" width="130" />
        <ElTableColumn prop="auditStatus" label="结果" width="110">
          <template #default="{ row }">
            <ElTag :type="row.auditStatus === 1 ? 'success' : 'danger'">
              {{ row.auditStatus === 1 ? '通过' : '驳回' }}
            </ElTag>
          </template>
        </ElTableColumn>
        <ElTableColumn prop="auditReason" label="审核意见" show-overflow-tooltip />
        <ElTableColumn prop="auditorId" label="审核人ID" width="120" />
        <ElTableColumn prop="auditedAt" label="审核时间" width="180" />
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
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { campusApi } from '@/service/api/campus';

const loading = ref(false);
const records = ref<any[]>([]);
const total = ref(0);
const query = reactive({ current: 1, size: 10, keyword: '' });
const auditForm = reactive({ targetType: 'post', targetId: 1, reason: '' });

async function fetchData() {
  loading.value = true;
  try {
    const data: any = await campusApi.page('/api/admin/audit', query);
    records.value = data?.records || [];
    total.value = data?.total || 0;
  } finally {
    loading.value = false;
  }
}

async function submitAudit(pass: boolean) {
  if (!auditForm.targetId) {
    ElMessage.warning('请填写内容ID');
    return;
  }
  if (!pass && !auditForm.reason) {
    ElMessage.warning('驳回时请填写审核意见');
    return;
  }
  await ElMessageBox.confirm(pass ? '确认审核通过该内容吗？' : '确认驳回该内容吗？', '提示', { type: 'warning' });
  const payload = { ...auditForm };
  if (pass) await campusApi.auditPass(payload);
  else await campusApi.auditReject(payload);
  ElMessage.success('审核操作完成');
  auditForm.reason = '';
  fetchData();
}

onMounted(fetchData);
</script>
