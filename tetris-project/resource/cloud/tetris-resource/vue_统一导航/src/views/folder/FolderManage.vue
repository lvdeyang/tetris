<template>
  <section>
    <el-tabs v-model="activeTabName" style="float:left;width:100%;">
      <el-tab-pane label="分组管理" name="分组管理"></el-tab-pane>
    </el-tabs>

    <el-card style="float:left;margin-top:10px;width:45%;font-size: 18px; min-height: 800px;" body-style="padding:0px">
      <div slot="header" class="clearfix">
        <div class="clearfix-first" style="position: relative; width: 100%; height: 37px;">
          <el-button type="primary" size="small" @click="showAddFolderDialog" :disabled="disableAddFolderBtn" style="float: left;">添加目录
          </el-button>
          <el-button type="primary" size="small" @click="showModifyFolderDialog" :disabled="disableModifyFolderBtn" style="float: left;">修改
          </el-button>
          <!-- <el-button type="danger" size="small" @click="upShift" style="float: left;">测试上移</el-button> -->
          <el-button type="danger" size="small" @click="deleteFolder" :disabled="disableDeleteFolderBtn" style="float: left;">删除
          </el-button>
          <!--<el-button type="danger" size="small" @click="resetRoot" :disabled="disableResetRootBtn"
                               style="float: left;">重置根目录
                    </el-button>-->
          <el-button type="danger" size="small" @click="releaseRoot" :disabled="disableReleaseRootBtn" style="float:left">解除根目录</el-button>
          <!-- <el-button type="primary" size="small" @click="showSetRootDialog" style="float: left;">设置根目录</el-button> -->

          <!--
                    <el-button type="primary" size="small" @click="downLoadFolderFromLdap"  style="float: left;margin-left: 40px;">从LDAP下载</el-button>
                    <el-button type="primary" size="small" @click="syncFolderToLdap"  style="float: left;">上传到LDAP</el-button>
                    <el-button type="primary" size="small" @click="cleanUpLdap"  style="float: left;">重置LDAP数据</el-button>
                    -->

          <el-dropdown style="float: left;margin-left: 10px;">
            <el-button type="primary" size="small" :loading="ldapLoading">
              用户及分组LDAP操作<i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>

            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item v-on:click.native="syncFolderToLdap()">上传到LDAP</el-dropdown-item>
              <el-dropdown-item v-on:click.native="downLoadFolderFromLdap()">从LDAP下载</el-dropdown-item>
              <el-dropdown-item v-on:click.native="cleanUpLdap()">重置LDAP数据</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>

        <div class="clearfix-second" style="position: relative;">
          <el-upload class="upload-folder" :action=uploadUrl name="filePoster" :headers="myHeaders" :show-file-list="false" accept=".csv" :on-success="uploadSuccess" :on-error="uploadError" :on-progress="onprogress" style="float: left;">
            <el-button type="primary" size="small">导入分组</el-button>
          </el-upload>

          <el-button type="primary" size="small" @click="exportFolder" style="float: left; margin-left: 10px;">导出分组
          </el-button>

          <el-upload class="upload-user" :action=uploadUserUrl name="userFile" :headers="myHeaders" :show-file-list="false" :on-success="uploadSuccess" accept=".csv" :on-error="uploadError" :on-progress="onprogress" :on-change="handleChange" style="float: left; margin-left: 10px;">
            <el-button type="primary" size="small">导入用户</el-button>
          </el-upload>

          <el-button type="primary" size="small" @click="exportUser" style="float: left; margin-left: 10px;">
            导出用户
          </el-button>
        </div>
      </div>

      <div class="custom-tree-container" v-loading="treeLoading">
        <div>

          <el-tree ref="tree" :data="tree" node-key="id" highlight-current :expand-on-click-node="false" :props="defaultProps" :default-expanded-keys="defaultExpandKeys" @node-click="handleNodeClick" @node-drag-end="handleNodeDragEnd" @node-expand="handleNodeExpand" @node-collapse="handleNodeCollapse" draggable :allow-drop="allowDrop" :allow-drag="allowDrag">
          </el-tree>

        </div>
      </div>
    </el-card>

    <el-card style="float:left;margin-left:50px;margin-top:10px;width:50%;min-height: 800px;" body-style="padding:0px">

      <div slot="header" class="clearfix">
        <el-select v-model="filters.deviceModel" size="small" placeholder="选择设备类型" style="float: left;margin-left: 30px;width:100px;">
          <el-option v-for="item in deviceModelOptions" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
        <el-input v-model="filters.keyword" size="small" style="float: left;margin-left: 30px;width:150px;" placeholder="关键字"></el-input>
        <el-button type="info" @click="getBundles" size="small" style="float: left;margin-left: 30px;">查询设备
        </el-button>
        <el-button type="info" @click="getUsers" size="small" style="float: left;margin-left: 30px;">查询用户
        </el-button>
        <el-button type="primary" @click="setFolder" size="small" :disabled="disableSetFolderBtn" style="float: right;margin-right: 50px;">加入分组
        </el-button>
      </div>

      <!--设备列表-->
      <el-table :data="resources" v-show="resourceTableShow" v-loading="resourceTableLoading" @selection-change="handleSelectionChange" style="width: 100%;">
        <el-table-column width="50" type="selection"></el-table-column>
        <el-table-column prop="bundleName" label="名称" width="200" sortable>
        </el-table-column>
        <el-table-column prop="deviceModel" label="资源类型" width="150" sortable>
        </el-table-column>
        <el-table-column prop="username" label="账号" width="200" sortable>
        </el-table-column>
        <el-table-column prop="bundleId" label="资源ID" width="300" sortable>
        </el-table-column>
      </el-table>

      <!--用户列表-->
      <el-table :data="users" v-show="userTableShow" v-loading="userTableLoading" @selection-change="handleSelectionChange" style="width: 100%;">
        <el-table-column width="50" type="selection"></el-table-column>
        <el-table-column prop="name" label="名称" width="200" sortable>
        </el-table-column>
        <el-table-column prop="userNo" label="用户编号" width="200" sortable>
        </el-table-column>
        <el-table-column prop="creater" label="创建者" width="250" sortable>
        </el-table-column>

      </el-table>

    </el-card>

    <!-- 添加分组对话框 -->
    <el-dialog title="添加分组" :visible.sync="dialogAddForderFormVisible" width="30%">
      <el-form :model="folderForm" label-width="100px">
        <el-form-item label="分组名称">
          <el-input v-model="folderForm.name" auto-complete="off" style="width: 300px;"></el-input>
        </el-form-item>

        <el-form-item label="上传LDAP">
          <el-select v-model="folderForm.toLdap" placeholder="是否上传LDAP">
            <el-option v-for="item in toLdapoptions" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogAddForderFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="addFolder">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 修改分组对话框 -->
    <el-dialog title="修改分组" :visible.sync="dialogmodifyForderFormVisible" width="30%">
      <el-form :model="modifyForm" label-width="100px">
        <el-form-item label="分组名称">
          <el-input v-model="modifyForm.name" auto-complete="off" style="width: 300px;"></el-input>
        </el-form-item>

        <el-form-item label="上传LDAP">
          <el-select v-model="modifyForm.toLdap" placeholder="是否上传LDAP">
            <el-option v-for="item in toLdapoptions" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>

      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogmodifyForderFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="modifyFolder">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 设置根目录对话框 -->
    <el-dialog title="设置根目录" :visible.sync="dialogsetRootVisible" width="30%">
      <div style="margin-top:10px;">
        <el-select v-model="setRootId" placeholder="选择根目录" style="float: left;margin-left: 30px;width:200px;">
          <el-option v-for="item in setRootOptions" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogsetRootVisible = false">取 消</el-button>
        <el-button type="primary" @click="setRoot">确 定</el-button>
      </div>
    </el-dialog>

  </section>
</template>

<script type="text/ecmascript-6">
import {  addFolder, modifyFolder, deleteFolder, setFolderOfBundles, resetFolderOfBundles, initFolderTree, changeFolderIndex, getDeviceModels,
  queryBundlesWithoutFolder, queryUsersWithoutFolder, setFolderToUsers, resetFolderOfUsers, queryRootOptions, setRoot, syncFolderToLdap,
  syncFolderFromLdap, cleanupFolderLdap, changeNodePosition, resetRootNode, releaseRoot, exportFolder, exportUser} from '../../api/api';

//let basePath = process.env.RESOURCE_ROOT;

let basePath = document.location.origin;

export default {
  data: function () {
    return {
      activeTabName: "FolderManage",
      resources: [],
      users: [],
      resourceTableShow: true,
      userTableShow: false,
      deviceModelOptions: [],
      filters: {
        deviceModel: 'jv210',
        keyword: ''
      },
      multipleSelection: [],
      disableSetFolderBtn: true,
      disableAddFolderBtn: true,
      disableModifyFolderBtn: true,
      disableDeleteFolderBtn: true,
      disableResetRootBtn: false,
      disableReleaseRootBtn: true,
      defaultExpandKeys: [],
      tree: [],
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      currentSelectNode: null,
      expandNodeKeysArr: [],
      resourceTableLoading: false,
      userTableLoading: false,
      dialogAddForderFormVisible: false,
      dialogmodifyForderFormVisible: false,
      folderForm: {
        name: "",
        toLdap: false
      },
      dialogsetRootVisible: false,
      setRootId: "",
      setRootOptions: [],
      modifyForm: {},
      uploadUrl: basePath + "/folder/import",
      uploadUserUrl: basePath + "/folder/user/import",
      myHeaders: {
        'tetris-001': sessionStorage.getItem('token')
      },
      treeLoading: false,
      toLdapoptions: [
        {
          value: true,
          label: "是"
        },
        {
          value: false,
          label: "否"
        }
      ],
      ldapLoading: false
    }
  },
  methods: {
    //获取资源类型
    getDeviceModels: function () {
      getDeviceModels().then(res => {
        if (!res.errMsg && res.deviceModels) {
          this.deviceModelOptions.push({
            value: "",
            label: "全部类型"
          });
          for (let deviceModel of res.deviceModels) {
            let deviceModelOption = {
              value: deviceModel,
              label: deviceModel
            };
            this.deviceModelOptions.push(deviceModelOption);
          }
        }
      });
    },

    handleChange (file, fileList) {
      var _this = this;
      if (file.raw) {
        let reader = new FileReader()
        reader.onload = function (e) {
          _this.contentHtml = e.target.result;
        };
        reader.readAsText(file.raw, 'utf-8');

      }
      console.log(file, fileList);
    },
    initTree: function (keepExpand) {
      this.treeLoading = true;
      initFolderTree().then(res => {
        if (res.errMsg) {
          this.$message({
            message: res.errMsg,
            type: 'error'
          });
          this.treeLoading = false;
        } else {
          this.tree = res.tree;
          // console.log(JSON.stringify(res.tree));

          if (!keepExpand) {
            // console.log('not keepExpand')
            this.expandNodeKeysArr.splice(0, this.expandNodeKeysArr.length);
            this.expandNodeKeysArr = this.tree.map(rootNode => rootNode.id);
          }

          this.defaultExpandKeys = this.expandNodeKeysArr;
          this.treeLoading = false;
          this.disableSetFolderBtn = true;
          this.disableAddFolderBtn = true;
          this.disableModifyFolderBtn = true;
          this.disableDeleteFolderBtn = true;
          this.disableResetRootBtn = false;
          this.disableReleaseRootBtn = true;
        }
      });
    },
    //获取资源列表
    getBundles: function () {
      this.resourceTableShow = true;
      this.userTableShow = false;
      let param = {
        deviceModel: this.filters.deviceModel,
        keyword: this.filters.keyword
      };
      this.resourceTableLoading = true;
      queryBundlesWithoutFolder(param).then((res) => {
        if (res.errMsg) {
          this.$message({
            message: res.errMsg,
            type: 'error'
          });
        } else {
          this.resources = res.resources;
        }

        this.resourceTableLoading = false;
      });
    },
    //获取未绑定的用户列表
    getUsers: function () {
      this.resourceTableShow = false;
      this.userTableShow = true;
      let param = {
        keyword: this.filters.keyword
      };
      // queryUsersWithoutFolder
      this.userTableLoading = true;
      queryUsersWithoutFolder(param).then((res) => {
        if (res.errMsg) {
          this.$message({
            message: res.errMsg,
            type: 'error'
          });
        } else {
          this.users = res.users;
        }

        this.userTableLoading = false;
      });

    },
    syncFolderToLdap: function () {
      this.$confirm('是否确认上传LDAP?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.resourceTableLoading = true;
        this.ldapLoading = true;
        let param = {};
        syncFolderToLdap(param).then(res => {
          this.resourceTableLoading = false;
          this.ldapLoading = false;
          if (res.errMsg) {
            this.$message({
              message: res.errMsg,
              type: 'error'
            });
          } else {
            this.$message({
              message: "同步成功数量：" + res.successCnt,
              type: 'success'
            });
            this.initTree(true);
          }
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消'
        });
      });
    },
    downLoadFolderFromLdap: function () {
      this.$confirm('是否确认从LDAP下载?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.resourceTableLoading = true;
        this.ldapLoading = true;
        let param = {};
        syncFolderFromLdap(param).then(res => {
          this.resourceTableLoading = false;
          this.ldapLoading = false;
          if (res.errMsg) {
            this.$message({
              message: res.errMsg,
              type: 'error'
            });
          } else {
            this.$message({
              message: "同步成功数量：" + res.successCnt,
              type: 'success'
            });
            this.initTree();
          }
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消'
        });
      });
    },
    cleanUpLdap: function () {
      this.$confirm('确认清空所有上传到LDAP和从LDAP下载的信息么？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.resourceTableLoading = true;
        this.ldapLoading = true;
        let param = {};
        cleanupFolderLdap(param).then(res => {
          this.resourceTableLoading = false;
          this.ldapLoading = false;
          if (res.errMsg) {
            this.$message({
              message: res.errMsg,
              type: 'error'
            });
          } else {
            this.$message({
              message: "操作成功",
              type: 'success'
            });
            this.initTree();
            this.getBundles();
          }
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消'
        });
      });
    },

    showAddFolderDialog: function () {
      this.dialogAddForderFormVisible = true;
    },
    addFolder: function () {
      if (!this.folderForm.name.length) {
        this.$message({
          message: "请输入分组名称",
          type: 'error'
        });
        return;
      }

      let param = {
        name: this.folderForm.name,
        parentId: this.currentSelectNode.id,
        toLdap: this.folderForm.toLdap
      };

      addFolder(param).then(res => {
        if (res.errMsg) {
          this.$message({
            message: res.errMsg,
            type: 'error'
          });
        } else {
          //this.currentSelectNode.children.push(res.folder);
          this.initTree(true)
          this.dialogAddForderFormVisible = false;
        }
      });
    },
    showModifyFolderDialog: function () {
      this.dialogmodifyForderFormVisible = true;
      this.modifyForm = {
        name: this.currentSelectNode.name,
        toLdap: this.currentSelectNode.toLdap
      };
      // this.folderForm = {};
      // this.folderForm.name = this.currentSelectNode.name;
      // this.folderForm.toLdap = this.currentSelectNode.toLdap;
    },
    modifyFolder: function () {
      if (!this.modifyForm.name.length) {
        this.$message({
          message: "请输入分组名称",
          type: 'error'
        });
        return;
      }

      // if(this.folderForm.name === this.currentSelectNode.name){
      //   return;
      // }

      let param = {
        id: this.currentSelectNode.id,
        name: this.modifyForm.name,
        toLdap: this.modifyForm.toLdap
      };

      modifyFolder(param).then(res => {
        if (res.errMsg) {
          this.$message({
            message: res.errMsg,
            type: 'error'
          });
        } else {
          this.initTree(true);
          this.dialogmodifyForderFormVisible = false;
        }
      });
    },
    deleteFolder: function () {
      if (this.currentSelectNode.beFolder) {
        this.treeLoading = true;
        //删除分组folder
        let param = {
          id: this.currentSelectNode.id
        };

        deleteFolder(param).then(res => {
          if (res.errMsg) {
            this.$message({
              message: res.errMsg,
              type: 'error'
            });
            this.treeLoading = false;
          } else {
            this.initTree(true);
          }
        });
      } else {
        //删除分组设备
        if (this.currentSelectNode.bundleId) {
          this.treeLoading = true;
          let param = {
            bundleIds: this.currentSelectNode.bundleId
          };
          resetFolderOfBundles(param).then(res => {
            if (res.errMsg) {
              this.$message({
                message: res.errMsg,
                type: 'error'
              });
              this.treeLoading = false;
            } else {
              this.initTree(true);
              this.getBundles();
            }
          });
        } else if (this.currentSelectNode.id) {
          this.treeLoading = true;
          let param = {
            userIds: this.currentSelectNode.id
          };
          resetFolderOfUsers(param).then(res => {
            if (res.errMsg) {
              this.$message({
                message: res.errMsg,
                type: 'error'
              });
              this.treeLoading = false;
            } else {
              this.initTree(true);
              this.getUsers();
            }
          });
        }

      }
    },

    resetRoot: function () {

      let param = {};

      resetRootNode(param).then(res => {
        if (res.errMsg) {
          this.$message({
            message: res.errMsg,
            type: 'error'
          });
        } else {
          this.initTree();
        }
      });


    },

    releaseRoot: function () {

      releaseRoot({
        folderId: this.currentSelectNode.id
      }).then(res => {
        if (res.errMsg) {
          this.$message({
            message: res.errMsg,
            type: 'error'
          });
        } else {
          this.$message({
            message: '操作成功！',
            type: 'success'
          });
          this.initTree();
        }
      });
    },


    handleNodeExpand: function (data, node) {
      this.expandNodeKeysArr.push(node.key);
    },

    handleNodeCollapse: function (data, node) {
      for (var i = 0; i < this.expandNodeKeysArr.length; i++) {

        if (this.expandNodeKeysArr[i] === node.key) {
          this.expandNodeKeysArr.splice(i, 1);
        }
      }
    },


    setFolder: function () {
      if (this.resourceTableShow) {
        let bundleIdArr = this.multipleSelection.map(bundle => bundle.bundleId);
        let param = {
          folderId: this.currentSelectNode.id,
          bundleIds: bundleIdArr.join(",")
        };

        setFolderOfBundles(param).then(res => {
          if (res.errMsg) {
            this.$message({
              message: res.errMsg,
              type: 'error'
            });
          } else {
            this.currentSelectNode.children = this.currentSelectNode.children.concat(res.bundleNodes);
            this.getBundles();
          }
        });
      } else if (this.userTableShow) {
        let userArr = this.multipleSelection;
        let param = {
          folderId: this.currentSelectNode.id,
          users: JSON.stringify(userArr)
        };

        setFolderToUsers(param).then(res => {
          if (res.errMsg) {
            this.$message({
              message: res.errMsg,
              type: 'error'
            });
          } else {
            //this.initTree(true);
            this.currentSelectNode.children = this.currentSelectNode.children.concat(res.userNodes);
            this.getUsers();
          }
        });
      }

    },

    handleSelectionChange: function (val) {
      this.multipleSelection = val;
      if (this.multipleSelection.length && this.currentSelectNode && this.currentSelectNode.beFolder) {
        //开启设置folder按钮
        this.disableSetFolderBtn = false;
      } else {
        this.disableSetFolderBtn = true;
      }
    },

    handleNodeClick: function (d, n, s) {
      //点击分组树节点
      this.currentSelectNode = d;
      if (d.beFolder) {
        //开启添加folder按钮
        this.disableAddFolderBtn = false;
        //开启修改folder按钮
        this.disableModifyFolderBtn = false;
        if (this.multipleSelection.length) {
          //开启设置folder按钮
          this.disableSetFolderBtn = false;
        }
        if ((d.children && d.children.length) || d.parentId < 0) {
          //禁止删除folder按钮
          this.disableDeleteFolderBtn = true;
        } else {
          //开启删除folder按钮
          this.disableDeleteFolderBtn = false;
        }
        if (d.parentId == -1) {
          this.disableReleaseRootBtn = false;
        } else {
          this.disableReleaseRootBtn = true;
        }
      } else {
        this.disableAddFolderBtn = true;
        this.disableModifyFolderBtn = true;
        this.disableSetFolderBtn = true;
        this.disableDeleteFolderBtn = false;
        this.disableReleaseRootBtn = true;
      }

      if (!d.systemSourceType) {
        this.disableAddFolderBtn = true;
        this.disableModifyFolderBtn = true;
        this.disableSetFolderBtn = true;
        this.disableDeleteFolderBtn = true;
        this.disableReleaseRootBtn = true;
      }
    },

    handleNodeDragEnd: function (draggingNode, dropNode, dropType, ev) {
      ev.stopPropagation();
      if (dropType === 'none') {
        return;
      }

      if (draggingNode.data.nodeType === 'USER' && dropNode.data.nodeType === 'USER') {
        if (dropType === 'inner') return false;
        console.log('交换两个用户的排序');
        console.log(dropType);
        changeFolderIndex({
          draggingId: draggingNode.data.id,
          dropId: dropNode.data.id,
          folderId: draggingNode.data.parentId,
          type: dropType
        }).then(res => {
          if (res.errMsg) {
            this.$message({
              message: res.errMsg,
              type: 'error'
            });
          }
          this.initTree(true);
        });
        return;
      }

      var nodeId;
      var newTargetId;

      if (draggingNode.data.nodeType === 'FOLDER') {
        nodeId = draggingNode.data.id;
      } else if (draggingNode.data.nodeType === 'BUNDLE') {
        nodeId = draggingNode.data.bundleId;
      } else if (draggingNode.data.nodeType === 'USER') {
        nodeId = draggingNode.data.username;

      }

      if (dropNode.data.nodeType === 'FOLDER') {
        newTargetId = dropNode.data.id;
      } else if (dropNode.data.nodeType === 'BUNDLE') {
        newTargetId = dropNode.data.bundleId;
      } else if (dropNode.data.nodeType === 'USER') {
        newTargetId = dropNode.data.username;
      }

      let param = {
        nodeId: nodeId,
        changeType: dropType,
        nodeType: draggingNode.data.nodeType,
        newTargetId: newTargetId,
        newTargetType: dropNode.data.nodeType,
        newTargetIndex: dropNode.data.folderIndex,
        newTargetParentId: dropNode.data.parentId
      };


      changeNodePosition(param).then(res => {
        if (res.errMsg) {
          this.$message({
            message: res.errMsg,
            type: 'error'
          });
        }

        console.log('change end, initTree')
        this.initTree(true);

      });
    },

    allowDrop (draggingNode, dropNode, type) {

      if (draggingNode.data.nodeType === 'USER' && dropNode.data.nodeType !== 'USER') {
        return false;
      } else {
        if (draggingNode.data.parentId !== dropNode.data.parentId) {
          return false;
        } else {
          if (type === 'inner') return false;
        }
      }

      if (draggingNode.id === dropNode.id) {
        return false;
      }

      if (type === 'none') {
        return false;
      }

      if ((dropNode.data.systemSourceType) && draggingNode.data.parentId === -1 && draggingNode.data.systemSourceType) {
        return false;
      }

      if (dropNode.data.folderType === 'ON_DEMAND') {
        return false;
      }

      if (dropNode.data.nodeType !== 'FOLDER') {

        if (dropNode.data.parentId !== -1) {
          var parentNode = this.$refs.tree.getNode(dropNode.data.parentId);

          if (parentNode.data.folderType === 'ON_DEMAND') {
            return false;
          }
        }
      }

      if ((type === 'next' || type === 'prev') && dropNode.data.parentId === -1) {
        return false;
      }

      return true;
    },

    allowDrag (draggingNode) {

      if (draggingNode.data.nodeType !== 'FOLDER' && draggingNode.data.nodeType !== 'USER') {
        return false;
      }


      if (draggingNode.data.parentId === -1 && !draggingNode.data.systemSourceType) {
        return false;
      }

      if (draggingNode.data.folderType === 'ON_DEMAND') {
        return false;
      }


      if (!draggingNode.data.systemSourceType) {
        return false;
      }
      return true;
    },

    showSetRootDialog: function () {
      this.dialogsetRootVisible = true;
      this.getRootoptions();
    },
    getRootoptions: function () {
      this.setRootOptions = [];
      queryRootOptions({}).then(res => {
        if (res.errMsg) {
          this.$message({
            message: res.errMsg,
            type: 'error'
          });
        } else {
          for (let rootOption of res.rootOptions) {
            let option = {
              "value": rootOption.id,
              "label": rootOption.name
            };
            this.setRootOptions.push(option);
          }
        }
      });
    },
    setRoot: function () {
      let param = {
        rootId: this.setRootId
      };
      setRoot(param).then(res => {
        if (res.errMsg) {
          this.$message({
            message: res.errMsg,
            type: 'error'
          });
        } else {
          this.dialogsetRootVisible = false;
          this.initTree();
        }
      });

    },

    uploadSuccess: function (res) {
      if (res.errMsg) {
        this.$message({
          message: res.errMsg,
          type: 'error'
        });
      } else {
        if (res.successCnt != null) {
          this.$message({
            message: "成功导入分组的数量：" + res.successCnt,
            type: 'success'
          });
        } else {
          this.$message({
            message: "导入失败",
            type: 'error'
          });
        }

        this.initTree(true);
      }
    },
    uploadError: function (err) {
      this.$message({
        message: "上传文件错误: " + JSON.stringify(err),
        type: 'error'
      });
    },
    onprogress: function () {
      //this.resourceTableLoading = true;
    },
    exportFolder: function () {

      exportFolder(null).then((res) => {
        const blob = new Blob([res.data], { type: 'application/octet-stream;charset=utf-8' });

        const fileName = 'Folder.csv';

        const elink = document.createElement('a');

        elink.download = fileName;

        elink.style.display = 'none';

        elink.href = URL.createObjectURL(blob);

        document.body.appendChild(elink);

        elink.click();

        URL.revokeObjectURL(elink.href);

        document.body.removeChild(elink);

      }).catch((error) => {

        this.$message({

          message: error,

          type: 'error'

        });

      })
    },
    exportUser: function () {

      exportUser(null).then((res) => {
        const blob = new Blob([res.data], { type: 'application/octet-stream;charset=utf-8' });

        const fileName = 'UserFolder.csv';

        const elink = document.createElement('a');

        elink.download = fileName;

        elink.style.display = 'none';

        elink.href = URL.createObjectURL(blob);

        document.body.appendChild(elink);

        elink.click();

        URL.revokeObjectURL(elink.href);

        document.body.removeChild(elink);

      }).catch((error) => {

        this.$message({

          message: error,

          type: 'error'

        });

      })
    }
  },

  mounted () {
    var self = this;
    self.$nextTick(function () {
      self.$parent.$parent.$parent.$parent.$parent.setActive('/FolderManage');
    });
    this.initTree();
    this.getDeviceModels();
    this.getBundles();
  }
}

</script>

<style>
.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both;
}
</style>
