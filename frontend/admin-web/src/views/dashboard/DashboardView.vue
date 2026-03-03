<template>
  <div class="dashboard-page">
    <a-typography-title :level="3">Dashboard</a-typography-title>
    <a-row :gutter="16">
      <a-col :xs="24" :md="8">
        <a-card>
          <a-statistic title="今日订单" :value="128" />
        </a-card>
      </a-col>
      <a-col :xs="24" :md="8">
        <a-card>
          <a-statistic title="待发货" :value="36" />
        </a-card>
      </a-col>
      <a-col :xs="24" :md="8">
        <a-card>
          <a-statistic title="今日销售额" :value="58320" prefix="¥" />
        </a-card>
      </a-col>
    </a-row>

    <a-card class="welcome-card">
      <a-typography-paragraph>
        欢迎进入运营后台。当前页面为基础 Dashboard，可在此扩展订单、商品、营销、用户等模块。
      </a-typography-paragraph>
    </a-card>
  </div>
</template>

<style scoped>
.dashboard-page {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.welcome-card {
  margin-top: 8px;
}
</style>
