<script setup lang="ts">
import {House, Setting} from "@element-plus/icons-vue"

const router = useRoute()
const activeIndex = ref(router.path)
const menuMeta = [
  {
    icon: House,
    name: "Home",
    path: "/"
  },
  {
    icon: Setting,
    name: "Config",
    path: "/config"
  }
]
</script>
<template>
  <el-menu :default-active="activeIndex">
    <el-menu-item
        v-for="(menu, index) in menuMeta"
        :id="index"
        @click="navigateTo(menu.path)"
        :index="menu.path"
    >
      <el-icon>
        <component :is="menu.icon"/>
      </el-icon>
      <span>{{ menu.name }}</span>
    </el-menu-item>
  </el-menu>
</template>
<style scoped>
.el-menu {
  background: #161819;
  line-height: 100vh;
  padding: 8px 0 8px 0;
  height: 100%;

  .el-menu-item {
    color: #fdfeff;
    margin: 8px;
    border-radius: 25px;

    &:hover {
      background: #408cff;
    }

    &.is-active {
      background: #408cff;
    }
  }
}
</style>