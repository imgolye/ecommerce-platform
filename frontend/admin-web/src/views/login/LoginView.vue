<template>
  <div class="login-page">
    <a-card class="login-card" title="运营后台登录">
      <a-form layout="vertical" :model="formState" @finish="onFinish">
        <a-form-item
          label="用户名"
          name="username"
          :rules="[{ required: true, message: '请输入用户名' }]"
        >
          <a-input v-model:value="formState.username" placeholder="请输入用户名" />
        </a-form-item>
        <a-form-item
          label="密码"
          name="password"
          :rules="[{ required: true, message: '请输入密码' }]"
        >
          <a-input-password v-model:value="formState.password" placeholder="请输入密码" />
        </a-form-item>
        <a-form-item>
          <a-button type="primary" html-type="submit" block>登录</a-button>
        </a-form-item>
      </a-form>
      <a-typography-text type="secondary">默认可输入任意账号密码进行演示登录</a-typography-text>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { reactive } from "vue";
import { useRouter } from "vue-router";
import { message } from "ant-design-vue";
import { useUserStore } from "@/stores/user";

const router = useRouter();
const userStore = useUserStore();

const formState = reactive({
  username: "",
  password: ""
});

const onFinish = () => {
  userStore.login({
    username: formState.username,
    token: "mock-admin-token"
  });
  message.success("登录成功");
  router.replace("/dashboard");
};
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(140deg, #e6f4ff 0%, #f5f5f5 60%, #fff 100%);
}

.login-card {
  width: 380px;
  border-radius: 12px;
}
</style>
