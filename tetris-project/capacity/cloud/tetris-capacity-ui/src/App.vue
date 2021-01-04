<template>
  <div id="app">
    <transition name="fade"
                mode="out-in">
      <router-view/>
    </transition>
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>
<style>
  .ant-select-dropdown-menu-item {
    height: 30px;
  }
  .ant-card-actions > li{
    margin: 6px 0px;
  }


</style>

