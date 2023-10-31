<script setup lang="ts">
import AppAside from "~/components/AppAside.vue";
import AppHeader from "~/components/AppHeader.vue";
</script>
<template>
  <el-container>
    <el-aside>
      <AppAside/>
    </el-aside>
    <el-container class="main">
      <el-header>
        <AppHeader/>
      </el-header>
      <el-main>
        <NuxtPage/>
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
.el-container {
  .el-aside {
    height: 100vh;
  }

  .main {
    margin-left: 12px;
  }
}
</style>