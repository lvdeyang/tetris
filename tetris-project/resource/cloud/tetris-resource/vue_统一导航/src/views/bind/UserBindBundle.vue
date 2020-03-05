<template>
    <div style="position:absolute; left:20px; top:20px; right:20px; bottom:20px;">
        <div style="width:100%; height:100%; position:relative;">
            <div style="position:absolute; left:0; top:0; bottom:0; width:400px; background-color:#fff; border-right:1px solid #EBEEF5; padding:10px; box-sizing:border-box;">
                <el-scrollbar style="height:100%;">
                    <el-tree
                            ref="userTree"
                            :props = "tree.props"
                            :data = "tree.data"
                            node-key="uuid"
                            check-strictly
                            :expand-on-click-node="false"
                            default-expand-all
                            highlight-current
                            @current-change="currentTreeNodeChange">

                            <span style="flex:1; display:flex; align-items:center; justify-content:space-between; padding-right:8px;" slot-scope="{node, data}">
                                <span style="font-size:14px;">
                                    <span v-if="data.type==='FOLDER'" class="icon-folder-open"></span>
                                    <span v-else class="icon-user"></span>
                                    <span>{{data.name}}</span>
                                </span>
                            </span>

                    </el-tree>
                </el-scrollbar>
            </div>
            <div v-if="tree.currentUser" style="position:absolute; left:401px; top:0; bottom:0; right:0; background-color:#fff; box-sizing:border-box; padding:10px;">
                <el-scrollbar style="height:100%;">
                    <div style="margin-bottom:22px;">
                        <el-button type="warning" size="small" @click="handleBindBundle">绑定设备</el-button>
                    </div>
                    <el-table
                            :data="table.data"
                            style="width:100%">
                        <el-table-column
                                prop="bundleName"
                                label="设备名称">
                        </el-table-column>
                        <el-table-column
                                prop="bundleId"
                                width="300"
                                label="bundleId">
                        </el-table-column>
                        <el-table-column
                                prop="username"
                                label="设备号码">
                        </el-table-column>
                        <el-table-column
                                prop="ip"
                                label="ip">
                        </el-table-column>
                        <el-table-column
                                prop="type"
                                label="类型">
                        </el-table-column>
                    </el-table>
                </el-scrollbar>
            </div>
        </div>

        <template>
            <el-dialog
                    title="绑定设备"
                    :visible.sync="dialog.bindBundle.visible"
                    width="500px"
                    :before-close="handleBindBundleClose">
                <el-form label-width="80px">
                    <el-form-item label="编码器">
                        <el-input v-model="form.encodeBundleName" readonly placeholder="点击选择设备" @click.native="handleSelectEncoder"></el-input>
                    </el-form-item>
                    <el-form-item label="解码器">
                        <el-input v-model="form.decodeBundleName" readonly placeholder="点击选择设备" @click.native="handleSelectDecoder"></el-input>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer">
                    <el-button size="medium" @click="handleBindBundleClose">取消</el-button>
                    <el-button size="medium" type="primary" @click="handleBindBundleCommit">确定</el-button>
                </span>
            </el-dialog>

            <el-dialog
                    title="选择编码器"
                    :visible.sync="dialog.selectEncoder.visible"
                    width="1000px"
                    :before-close="handleSelectEncoderClose">

                <div style="height:450px; position:relative;">
                    <div style="position:absolute; left:-20px; top:-20px; right:-20px; bottom:-20px;">
                        <div style="width:100%; height:100%; position:relative;">
                            <div style="width:100%; height:40px; padding-left:20px; padding-right:20px; box-sizing:border-box; position:relative; bottom:5px;">
                                <el-form :inline="true" class="demo-form-inline">
                                    <el-form-item label="名称">
                                        <el-input size="small" style="width:173px;" v-model="dialog.selectEncoder.condition.bundleName" placeholder="名称"></el-input>
                                    </el-form-item>
                                    <el-form-item label="号码">
                                        <el-input size="small" style="width:173px;" v-model="dialog.selectEncoder.condition.bundleNo" placeholder="号码"></el-input>
                                    </el-form-item>
                                    <el-form-item label="ip">
                                        <el-input size="small" style="width:173px;" v-model="dialog.selectEncoder.condition.ip" placeholder="ip"></el-input>
                                    </el-form-item>
                                    <el-form-item label="bundleId">
                                        <el-input size="small" style="width:173px;" v-model="dialog.selectEncoder.condition.bundleId" placeholder="bundleId"></el-input>
                                    </el-form-item>
                                    <el-form-item>
                                        <el-button style="width:32px; padding:0; height:32px; font-size:16px; position:relative; top:1px;" size="small" type="primary" @click="handleRefreshEncoder" icon="el-icon-search"></el-button>
                                    </el-form-item>
                                </el-form>
                            </div>
                            <div style="width:100%; position:absolute; left:0; top:40px; right:0; bottom:0; padding:0 10px; box-sizing:border-box;">
                                <el-table
                                        ref="selectEncoderTable"
                                        :data="dialog.selectEncoder.table.data"
                                        height="100%"
                                        style="width:100%"
                                        highlight-current-row
                                        row-key="selectEncoderTableRowKey"
                                        @current-change="selectEncoderCurrentChange">
                                    <el-table-column
                                            prop="bundleName"
                                            label="设备名称">
                                    </el-table-column>
                                    <el-table-column
                                            prop="bundleId"
                                            width="300"
                                            label="bundleId">
                                    </el-table-column>
                                    <el-table-column
                                            prop="username"
                                            label="设备号码">
                                    </el-table-column>
                                    <el-table-column
                                            prop="ip"
                                            label="ip">
                                    </el-table-column>
                                </el-table>
                            </div>
                        </div>
                    </div>
                </div>

                <span slot="footer" class="dialog-footer">
                    <el-pagination
                            style="float:left;"
                            background
                            layout="total, prev, pager, next, jumper"
                            :pageSize="dialog.selectEncoder.table.page.pageSize"
                            :total="dialog.selectEncoder.table.page.total">
                    </el-pagination>
                    <el-button size="small" @click="handleSelectEncoderClose">取消</el-button>
                    <el-button size="small" type="primary" @click="handleSelectEncoderCommit">确定</el-button>
                </span>
            </el-dialog>

            <el-dialog
                    title="选择解码器"
                    :visible.sync="dialog.selectDecoder.visible"
                    width="1000px"
                    :before-close="handleSelectDecoderClose">

                <div style="height:450px; position:relative;">
                    <div style="position:absolute; left:-20px; top:-20px; right:-20px; bottom:-20px;">
                        <div style="width:100%; height:100%; position:relative;">
                            <div style="width:100%; height:40px; padding-left:20px; padding-right:20px; box-sizing:border-box; position:relative; bottom:5px;">
                                <el-form :inline="true" class="demo-form-inline">
                                    <el-form-item label="名称">
                                        <el-input size="small" style="width:173px;" v-model="dialog.selectDecoder.condition.bundleName" placeholder="名称"></el-input>
                                    </el-form-item>
                                    <el-form-item label="号码">
                                        <el-input size="small" style="width:173px;" v-model="dialog.selectDecoder.condition.bundleNo" placeholder="号码"></el-input>
                                    </el-form-item>
                                    <el-form-item label="ip">
                                        <el-input size="small" style="width:173px;" v-model="dialog.selectDecoder.condition.ip" placeholder="ip"></el-input>
                                    </el-form-item>
                                    <el-form-item label="bundleId">
                                        <el-input size="small" style="width:173px;" v-model="dialog.selectDecoder.condition.bundleId" placeholder="bundleId"></el-input>
                                    </el-form-item>
                                    <el-form-item>
                                        <el-button style="width:32px; padding:0; height:32px; font-size:16px; position:relative; top:1px;" size="small" type="primary" @click="handleRefreshDecoder" icon="el-icon-search"></el-button>
                                    </el-form-item>
                                </el-form>
                            </div>
                            <div style="width:100%; position:absolute; left:0; top:40px; right:0; bottom:0; padding:0 10px; box-sizing:border-box;">
                                <el-table
                                        ref="selectDecoderTable"
                                        :data="dialog.selectDecoder.table.data"
                                        height="100%"
                                        row-key="selectDecoderTableRowKey"
                                        style="width:100%"
                                        highlight-current-row
                                        @current-change="selectDecoderCurrentChange">
                                    <el-table-column
                                            prop="bundleName"
                                            label="设备名称">
                                    </el-table-column>
                                    <el-table-column
                                            prop="bundleId"
                                            width="300"
                                            label="bundleId">
                                    </el-table-column>
                                    <el-table-column
                                            prop="username"
                                            label="设备号码">
                                    </el-table-column>
                                    <el-table-column
                                            prop="ip"
                                            label="ip">
                                    </el-table-column>
                                </el-table>
                            </div>
                        </div>
                    </div>
                </div>

                <span slot="footer" class="dialog-footer">
                    <el-pagination
                            style="float:left;"
                            background
                            layout="total, prev, pager, next, jumper"
                            :pageSize="dialog.selectDecoder.table.page.pageSize"
                            :total="dialog.selectDecoder.table.page.total">
                    </el-pagination>
                    <el-button size="small" @click="handleSelectDecoderClose">取消</el-button>
                    <el-button size="small" type="primary" @click="handleSelectDecoderCommit">确定</el-button>
                </span>
            </el-dialog>

        </template>

    </div>

</template>
<script type="text/ecmascript-6">

import{
    getAllUsers,
    queryUserBindBundles,
    setUserBindBundles,
    queryEncoders,
    queryDecoders
}from '../../api/api'

export default{
    data:function(){
        return {
            form:{
                encodeId:'',
                encodeBundleId:'',
                encodeBundleName:'',
                encodeDeviceModel:'',
                encodeUsername:'',
                encodeIp:'',
                decodeId:'',
                decodeBundleId:'',
                decodeBundleName:'',
                decodeDeviceModel:'',
                decodeUsername:'',
                decodeIp:''
            },
            tree:{
                props:{
                    label:'name',
                    children:'children',
                    isLeaf:'isLeaf'
                },
                data:[],
                currentUser:''
            },
            table:{
                data:[]
            },
            dialog:{
                bindBundle:{
                    visible:false
                },
                selectEncoder:{
                    visible:false,
                    condition:{
                        bundleName:'',
                        bundleNo:'',
                        ip:'',
                        bundleId:''
                    },
                    table:{
                        data:[],
                        page:{
                            total:0,
                            currentPage:0,
                            pageSize:50
                        },
                        current:''
                    }
                },
                selectDecoder:{
                    visible:false,
                    condition:{
                        bundleName:'',
                        bundleNo:'',
                        ip:'',
                        bundleId:''
                    },
                    table:{
                        data:[],
                        page:{
                            total:0,
                            currentPage:0,
                            pageSize:50
                        },
                        current:''
                    }
                }
            }
        }
    },
    methods: {
        refreshUser:function(){
            var self = this;
            getAllUsers().then(res => {
                if(res.errMsg){
                    self.$message({
                        message: res.errMsg,
                        type: 'error'
                    });
                }else{
                    self.tree.data.splice(0, self.tree.data.length);
                    self.tree.data.push({
                        id:0,
                        uuid:'FOLDER_0',
                        type:'FOLDER',
                        name:'用户列表',
                        children:[],
                        isLeaf:false
                    });
                    if(res.users && res.users.length>0){
                        for(var i=0; i<res.users.length; i++){
                            self.tree.data[0].children.push({
                                id:res.users[i].id,
                                uuid:'USER_'+res.users[i].id,
                                name:res.users[i].name,
                                type:'USER',
                                userNo:res.users[i].userNo,
                                isLeaf:true
                            });
                        }
                    }
                }
            });
        },
        currentTreeNodeChange:function(data){
            var self = this;
            self.table.data.splice(0, self.table.data.length);
            if(data.type === 'FOLDER'){
                self.tree.currentUser = '';
            }else{
                self.tree.currentUser = data;
                queryUserBindBundles({
                    userId:data.id
                }).then(response => {
                    if(response.status !== 200){
                        self.$message({
                            message:response.message,
                            type:'error'
                        });
                    }else{
                        var data = response.data;
                        if(data.userId){
                            self.form.encodeId = data.encodeId;
                            self.form.encodeBundleId = data.encodeBundleId;
                            self.form.encodeBundleName = data.encodeBundleName;
                            self.form.encodeDeviceModel = data.encodeDeviceModel;
                            self.form.encodeUsername = data.encodeUsername;
                            self.form.encodeIp = data.encodeIp;
                            self.form.decodeId = data.decodeId;
                            self.form.decodeBundleId = data.decodeBundleId;
                            self.form.decodeBundleName = data.decodeBundleName;
                            self.form.decodeDeviceModel = data.decodeDeviceModel;
                            self.form.decodeUsername = data.decodeUsername;
                            self.form.decodeIp = data.decodeIp;
                            self.table.data.push({
                                bundleId:data.encodeBundleId,
                                bundleName:data.encodeBundleName,
                                deviceModel:data.encodeDeviceModel,
                                username:data.encodeUserName,
                                ip:data.encodeIp,
                                type:'编码器'
                            });
                            self.table.data.push({
                                bundleId:data.decodeBundleId,
                                bundleName:data.decodeBundleName,
                                deviceModel:data.decodeDeviceModel,
                                username:data.decodeUserName,
                                ip:data.decodeIp,
                                type:'解码器'
                            });
                        }else{
                            self.form.encodeId = '',
                            self.form.encodeBundleId = '',
                            self.form.encodeBundleName = '',
                            self.form.encodeDeviceModel = '',
                            self.form.encodeUsername = '';
                            self.form.encodeIp = '';
                            self.form.decodeId = '',
                            self.form.decodeBundleId = '';
                            self.form.decodeBundleName = '';
                            self.form.decodeDeviceModel = '';
                            self.form.decodeUsername = '';
                            self.form.decodeIp = '';
                        }
                    }
                });
            }
        },
        handleBindBundle:function(){
            var self = this;
            self.dialog.bindBundle.visible = true;
        },
        handleBindBundleClose:function(){
            var self = this;
            self.dialog.bindBundle.visible = false;
        },
        handleBindBundleCommit:function(){
            var self = this;
            setUserBindBundles({
                userId:self.tree.currentUser.id,
                userName:self.tree.currentUser.name,
                encodeId:self.form.encodeId,
                encodeBundleId:self.form.encodeBundleId,
                encodeBundleName:self.form.encodeBundleName,
                encodeDeviceModel:self.form.encodeDeviceModel,
                decodeId:self.form.decodeId,
                decodeBundleId:self.form.decodeBundleId,
                decodeBundleName:self.form.decodeBundleName,
                decodeDeviceModel:self.form.decodeDeviceModel
            }).then(response=>{
                if(response.status !== 200){
                    self.$message({
                        message:response.message,
                        type:'error'
                    });
                }else{
                    self.table.data.splice(0, self.table.data.length);
                    self.table.data.push({
                        bundleId:self.form.encodeBundleId,
                        bundleName:self.form.encodeBundleName,
                        deviceModel:self.form.encodeDeviceModel,
                        username:self.form.encodeUsername,
                        ip:self.form.encodeIp,
                        type:'编码器'
                    });
                    self.table.data.push({
                        bundleId:self.form.decodeBundleId,
                        bundleName:self.form.decodeBundleName,
                        deviceModel:self.form.decodeDeviceModel,
                        username:self.form.decodeUsername,
                        ip:self.form.decodeIp,
                        type:'解码器'
                    });
                    self.handleBindBundleClose();
                }
            });
        },

        refreshEncoder:function(currentPage){
            var self = this;
            self.dialog.selectEncoder.table.data.splice(0, self.dialog.selectEncoder.table.data.length);
            queryEncoders({
                currentPage:currentPage,
                pageSize:self.dialog.selectEncoder.table.page.pageSize,
                bundleName:self.dialog.selectEncoder.condition.bundleName || null,
                username:self.dialog.selectEncoder.condition.bundleNo || null,
                ip:self.dialog.selectEncoder.condition.ip || null,
                bundleId:self.dialog.selectEncoder.condition.bundleId || null
            }).then(response => {
                if(response.status !== 200){
                    self.$message({
                        message:response.message,
                        type:'error'
                    });
                }else{
                    var data = response.data;
                    var total = data.total;
                    var rows = data.rows;
                    if(rows && rows.length>0){
                        for(var i=0; i<rows.length; i++){
                            self.dialog.selectEncoder.table.data.push(rows[i]);
                        }
                    }
                    self.dialog.selectEncoder.table.page.total = total || 0;
                    self.dialog.selectEncoder.table.page.currentPage = currentPage;
                }
            });
        },
        selectEncoderTableRowKey:function(row){
            return row.bundleId;
        },
        handleRefreshEncoder:function(){
            var self = this;
            self.refreshEncoder(1);
        },
        handleSelectEncoder:function(){
            var self = this;
            self.dialog.selectEncoder.visible = true;
            self.refreshEncoder(1);
        },
        handleSelectEncoderClose:function(){
            var self = this;
            self.dialog.selectEncoder.visible = false;

            self.dialog.selectEncoder.condition.bundleName = '';
            self.dialog.selectEncoder.condition.bundleNo = '';
            self.dialog.selectEncoder.condition.ip = '';
            self.dialog.selectEncoder.condition.bundleId = '';

            self.dialog.selectEncoder.table.data.splice(0, self.dialog.selectEncoder.table.data.length);
            self.dialog.selectEncoder.table.page.total = 0;
            self.dialog.selectEncoder.table.page.currentPage = 0;
            self.dialog.selectEncoder.table.current = '';

        },
        handleSelectEncoderCommit:function(){
            var self = this;
            self.form.encodeId = self.dialog.selectEncoder.table.current.id;
            self.form.encodeBundleId = self.dialog.selectEncoder.table.current.bundleId;
            self.form.encodeBundleName = self.dialog.selectEncoder.table.current.bundleName;
            self.form.encodeDeviceModel = self.dialog.selectEncoder.table.current.deviceModel;
            self.form.encodeUsername = self.dialog.selectEncoder.table.current.username;
            self.form.encodeIp = self.dialog.selectEncoder.table.current.ip;
            self.handleSelectEncoderClose();
        },
        handleSelectEncoderCurrentChange:function(currentPage){
            var self = this;
            self.refreshEncoder(currentPage);
        },
        selectEncoderCurrentChange:function(currentRow){
            var self = this;
            self.dialog.selectEncoder.table.current = currentRow;
        },

        refreshDecoder:function(currentPage){
            var self = this;
            self.dialog.selectDecoder.table.data.splice(0, self.dialog.selectDecoder.table.data.length);
            queryDecoders({
                currentPage:currentPage,
                pageSize:self.dialog.selectDecoder.table.page.pageSize,
                bundleName:self.dialog.selectDecoder.condition.bundleName || null,
                username:self.dialog.selectDecoder.condition.bundleNo || null,
                ip:self.dialog.selectDecoder.condition.ip || null,
                bundleId:self.dialog.selectDecoder.condition.bundleId || null
            }).then(response => {
                if(response.status !== 200){
                    self.$message({
                        message:response.message,
                        type:'error'
                    });
                }else{
                    var data = response.data;
                    var total = data.total;
                    var rows = data.rows;
                    if(rows && rows.length>0){
                        for(var i=0; i<rows.length; i++){
                            self.dialog.selectDecoder.table.data.push(rows[i]);
                        }
                    }
                    self.dialog.selectDecoder.table.page.total = total || 0;
                    self.dialog.selectDecoder.table.page.currentPage = currentPage;
                }
            });
        },
        selectDecoderTableRowKey:function(row){
            return row.bundleId;
        },
        handleRefreshDecoder:function(){
            var self = this;
            self.refreshDecoder(1);
        },
        handleSelectDecoder:function(){
            var self = this;
            self.dialog.selectDecoder.visible = true;
            self.refreshDecoder(1);
        },
        handleSelectDecoderClose:function(){
            var self = this;
            self.dialog.selectDecoder.visible = false;

            self.dialog.selectDecoder.condition.bundleName = '';
            self.dialog.selectDecoder.condition.bundleNo = '';
            self.dialog.selectDecoder.condition.ip = '';
            self.dialog.selectDecoder.condition.bundleId = '';

            self.dialog.selectDecoder.table.data.splice(0, self.dialog.selectDecoder.table.data.length);
            self.dialog.selectDecoder.table.page.total = 0;
            self.dialog.selectDecoder.table.page.currentPage = 0;
            self.dialog.selectDecoder.table.current = '';
        },
        handleSelectDecoderCommit:function(){
            var self = this;
            self.form.decodeId = self.dialog.selectDecoder.table.current.id;
            self.form.decodeBundleId = self.dialog.selectDecoder.table.current.bundleId;
            self.form.decodeBundleName = self.dialog.selectDecoder.table.current.bundleName;
            self.form.decodeDeviceModel = self.dialog.selectDecoder.table.current.deviceModel;
            self.form.decodeUsername = self.dialog.selectDecoder.table.current.username;
            self.form.decodeIp = self.dialog.selectDecoder.table.current.ip;
            self.handleSelectDecoderClose();
        },
        handleSelectDecoderCurrentChange:function(currentPage){
            var self = this;
            self.refreshDecoder(currentPage);
        },
        selectDecoderCurrentChange:function(currentRow){
            var self = this;
            self.dialog.selectDecoder.table.current = currentRow;
        }

    },
    mounted:function(){
        var self = this;
        self.$nextTick(function(){
            self.$parent.$parent.$parent.$parent.$parent.setActive('/UserBindBundle');
        });
        self.refreshUser();
    }
}

</script>
<style scoped type="text/css">
    .el-scrollbar__wrap{overflow-x:hidden;}
</style>
