<template>
    <div style="height:100%" class="card-container">
      <a-tabs type="card">
        <a-tab-pane key="1" tab="网卡分组">
           <net-group></net-group>
        </a-tab-pane>
        <a-tab-pane key="2" tab="自动备份条件">
          <backup-condition></backup-condition>
        </a-tab-pane>
      </a-tabs>
    </div>
</template>

<script>
  import NetGroup from './NetGroup'
  import BackupCondition from './BackupCondition'
    export default {
        name: "ConfigManager",
      data(){
          return {

          }
      },
      components: {
          NetGroup,BackupCondition
      }
    }
</script>

<style scoped>

</style>
