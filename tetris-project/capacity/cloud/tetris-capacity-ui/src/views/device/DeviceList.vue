<template>
    <div>
      <a-card  hoverable  style="height: 720px;">
        <div slot="title">设备列表</div>
        <div id="components-form-demo-advanced-search">
          <a-form class="ant-advanced-search-form" :form="form" @submit="handleSearch" layout="inline">
            <a-row >
              <a-col :span="5" :style="{ display: 0 < count ? 'block' : 'none' }">
                <a-form-item label="设备名称">
                  <a-input />
                </a-form-item>
              </a-col>
              <a-col :span="5" :style="{ display: 1 < count ? 'block' : 'none' }">
                <a-form-item label="设备IP">
                  <a-input />
                </a-form-item>
              </a-col>
              <a-col :span="4" :style="{ display: 2 < count ? 'block' : 'none' }">
                <a-form-item label="主备状态">
                  <a-select style="width: 100px;">
                    <a-select-option value="NONE">不限</a-select-option>
                    <a-select-option value="MAIN">主</a-select-option>
                    <a-select-option value="BACK">备</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :span="4" :style="{ display: 3 < count ? 'block' : 'none' }">
                <a-form-item label="设备状态">
                  <a-select style="width: 100px;">
                    <a-select-option value="NONE">不限</a-select-option>
                    <a-select-option value="MAIN">在线</a-select-option>
                    <a-select-option value="BACK">离线</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :span="6" :style="{ textAlign: 'right' }">
                <a-button type="primary" html-type="submit">
                  搜索
                </a-button>
                <a-button :style="{ marginLeft: '8px' }" @click="handleReset">
                  清空
                </a-button>
                <a :style="{ marginLeft: '8px', fontSize: '12px' }" @click="toggle">
                  展开 <a-icon :type="expand ? 'up' : 'down'" />
                </a>
              </a-col>
            </a-row>
            <a-row>

            </a-row>
          </a-form>
          <div class="search-result-list">
            Search Result List
          </div>
        </div>
      </a-card>
    </div>
</template>

<script>
    export default {
        name: "DeviceList",
      data(){
          return {
            expand: false,
            form: this.$form.createForm(this, { name: 'advanced_search' }),
          }
      },
      computed: {
        count() {
          return this.expand ? 8 : 8;
        },
      },
      updated() {
        console.log('updated');
      },
      methods: {
        handleSearch(e) {
          e.preventDefault();
          this.form.validateFields((error, values) => {
            console.log('error', error);
            console.log('Received values of form: ', values);
          });
        },
        handleReset() {
          this.form.resetFields();
        },
        toggle() {
          this.expand = !this.expand;
        },
      },
    }
</script>

<style scoped>

</style>
