<template>
  <div>
    <a-card>
      <a-button @click="visibleAddNetGroupModal=true" icon="plus" type="primary" style="margin-bottom:10px;">添加分组</a-button>
      <a-table bordered :dataSource="netGroups" :columns="netGroupColumns" :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }">
        <template slot="action" slot-scope="text,record,index">
          <a-button type="primary" icon="edit"  @click="handleGroupEdit(record)">修改</a-button>
          <a-divider type="vertical"></a-divider>
          <a-button type="danger" icon="delete"   @click="handleGroupDelete(record)">删除</a-button>
        </template>
      </a-table>
    </a-card>
    <!--添加设备分组对话框-->
    <a-modal title="添加设备分组" v-model="visibleAddNetGroupModal" @cancel="closeAddNetGroupModal" @ok="saveNetGroup">
      <a-form :label-col="{ span: 6 }" :wrapper-col="{ span: 12 }">
        <a-form-item label="分组名称">
          <a-input v-model="addNetGroupObj.netName"></a-input>
        </a-form-item>
        <a-form-item label="描述信息">
          <a-input v-model="addNetGroupObj.info"></a-input>
        </a-form-item>
        <a-form-item label="分组类型">
          <a-select v-model="addNetGroupObj.netType">
            <a-select-option value="INPUT">输入</a-select-option>
            <a-select-option value="OUTPUT">输出</a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>
    <!--修改网卡分组-->
    <a-modal title="添加设备分组" v-model="visibleEditNetGroupModal" @cancel="closeEditNetGroupModal" @ok="modifyNetGroup">
      <a-form :label-col="{ span: 6 }" :wrapper-col="{ span: 12 }">
        <a-form-item label="分组名称">
          <a-input v-model="editNetGroupObj.netName"></a-input>
        </a-form-item>
        <a-form-item label="描述信息">
          <a-input v-model="editNetGroupObj.info"></a-input>
        </a-form-item>
        <a-form-item label="分组类型">
          <a-select v-model="editNetGroupObj.netType">
            <a-select-option value="INPUT">输入</a-select-option>
            <a-select-option value="OUTPUT">输出</a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>

</template>

<script>
  import {queryAllNetGroup,beUsedNetGroup,deleteNetGroup,editNetGroup,addNetGroup} from '../../api/api'
    export default {
        name: "NetGroup",
      data(){
          return {
            netGroups:[],
            selectedRowKeys: [],
            netGroupColumns:[
              {dataIndex:'netName',key:'netName',title:'名称'},
              {dataIndex:'info',key:'info',title:'描述'},
              {dataIndex:'netType',key:'netType',title:'类型'},
              {dataIndex:'action',key:'action',title:'操作',scopedSlots: {customRender: 'action'}}
            ],
            addNetGroupObj:{
              netName:'',
              info:'',
              netType:''
            },
            editNetGroupObj:{},
            editNetGroup:{},
            visibleAddNetGroupModal:false,
            visibleEditNetGroupModal:false,
          }
      },
      methods:{
        closeAddNetGroupModal(){
          this.addNetGroupObj={}
          this.visibleAddNetGroupModal=false;
        },
        closeEditNetGroupModal(){
          this.editNetGroupObj={}
          this.visibleEditNetGroupModal=false;
        },
        onSelectChange(selectedRowKeys) {
          console.log('selectedRowKeys changed: ', selectedRowKeys);
          this.selectedRowKeys = selectedRowKeys;
        },
        handleGroupDelete(record){
          let self=this
          beUsedNetGroup({id: record.id}).then((res) => {
            if(res.status !== 200) {
              self.$message.error("分组信息查询异常!")
            }else{
              if (res.data){
                self.$message.error("分组正在使用，无法删除")
              } else{
                deleteNetGroup({id:record.id}).then(res=>{
                  if (res.status!=200){
                    this.$message.error(res.message);
                  }else {
                    this.$message.info('操作成功');
                    this.fetchAllNetGroups();
                  }
                })
              }
            }
          });

        },
        saveNetGroup(){
          addNetGroup(this.addNetGroupObj).then(res => {
            if (res.status!=200){
              this.$message.error(res.message);
            }else {
              this.$message.info('操作成功');
              this.fetchAllNetGroups();
              this.closeAddNetGroupModal();
            }
          })
        },
        modifyNetGroup(){
          editNetGroup(this.editNetGroupObj).then(res => {
            if (res.status!=200){
              this.$message.error(res.message);
            }else {
              this.$message.info('操作成功');
              this.fetchAllNetGroups();
              this.closeEditNetGroupModal();
            }
          })
        },
        handleGroupEdit(record) {
          let self = this;
          beUsedNetGroup({id: record.id}).then((res) => {
            if(res.status !== 200) {
              self.$message.error("分组信息查询异常!")
              return;
            }
            if (res.data){
              self.$message.error("分组正在使用，不能修改！")
              return;
            }else{
              self.editNetGroupObj = record
            }
            self.visibleEditNetGroupModal = true;
          });
        },
        fetchAllNetGroups(){
          queryAllNetGroup({}).then(res=>{
            if (res.status!=200){
              this.$message.error(res.message);
            }else {
              this.netGroups = res.data
            }
          })
        }
      },
      created(){
        this.fetchAllNetGroups()
      }
    }
</script>

<style scoped>

</style>
