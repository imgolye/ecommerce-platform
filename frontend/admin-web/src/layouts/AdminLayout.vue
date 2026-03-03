<template>
  <a-layout class="admin-layout">
    <a-layout-sider
      v-model:collapsed="collapsed"
      :trigger="null"
      collapsible
      width="220"
      class="layout-sider"
    >
      <div class="brand">{{ collapsed ? "运营" : "电商运营后台" }}</div>
      <a-menu
        theme="dark"
        mode="inline"
        :selected-keys="[selectedKey]"
        @click="onMenuClick"
      >
        <a-menu-item key="/dashboard">
          <DashboardOutlined />
          <span>首页</span>
        </a-menu-item>
      </a-menu>
    </a-layout-sider>

    <a-layout>
      <a-layout-header class="layout-header">
        <div class="header-left">
          <MenuUnfoldOutlined v-if="collapsed" class="trigger" @click="collapsed = false" />
          <MenuFoldOutlined v-else class="trigger" @click="collapsed = true" />
        </div>
        <div class="header-right">
          <a-dropdown>
            <a class="user-name" @click.prevent>
              {{ userStore.username || "管理员" }}
              <DownOutlined />
            </a>
            <template #overlay>
              <a-menu>
                <a-menu-item key="logout" @click="handleLogout">退出登录</a-menu-item>
              </a-menu>
            </template>
          </a-dropdown>
        </div>
      </a-layout-header>

      <a-layout-content class="layout-content">
        <RouterView />
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>

<script setup lang="ts">
import { computed, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import {
  DashboardOutlined,
  DownOutlined,
  MenuFoldOutlined,
  MenuUnfoldOutlined
} from "@ant-design/icons-vue";
import { useUserStore } from "@/stores/user";

const route = useRoute();
const router = useRouter();
const userStore = useUserStore();
const collapsed = ref(false);

const selectedKey = computed(() => {
  if (route.path.startsWith("/dashboard")) {
    return "/dashboard";
  }
  return route.path;
});

const onMenuClick = ({ key }: { key: string }) => {
  router.push(key);
};

const handleLogout = () => {
  userStore.logout();
  router.replace("/login");
};
</script>

<style scoped>
.admin-layout {
  min-height: 100vh;
}

.layout-sider {
  box-shadow: 2px 0 8px rgb(0 0 0 / 12%);
}

.brand {
  height: 56px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 16px;
  font-weight: 600;
}

.layout-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 16px;
  background: #fff;
  border-bottom: 1px solid #f0f0f0;
}

.trigger {
  font-size: 18px;
  cursor: pointer;
}

.header-right .user-name {
  color: rgb(0 0 0 / 88%);
}

.layout-content {
  margin: 16px;
  padding: 16px;
  background: #fff;
  border-radius: 8px;
  min-height: calc(100vh - 88px);
}
</style>
