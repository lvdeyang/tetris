<template>
  <section>
    <el-tabs v-model="activeTabName" @tab-click="handleTabClick" style="float:left; width:100%;">
      <el-tab-pane label="资源列表" name="LwLocalBundleManage"></el-tab-pane>
      <!-- <el-tab-pane label="外域资源列表" name="BundleManage"></el-tab-pane> -->
      <el-tab-pane label="修改资源" name="LwModifyBundle"></el-tab-pane>
    </el-tabs>

    <el-form label-width="100px" :inline="true">
      <el-form-item size="small" label="设备名称" prop="bundleName">
        <el-input v-model="bundleName" style="width: 200px;" placeholder="输入新的设备名称"></el-input>
      </el-form-item>
      <el-form-item size="small" label="设备形态" prop="deviceModel">
        <el-select size="small" v-model="deviceModel" style="width: 200px;" @change="deviceModelChange">
          <el-option disabled v-for="item in deviceModelOptions" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item size="small" label="设备类型" v-if="deviceModel =='jv210'">
        <el-select v-model="extraParam.dev_type" placeholder="请选择" style="width: 200px;" @change="devTypeChange">
          <template v-for="item in devTypeOption">
            <el-option disabled :label="item.label" :value="item.value" :key="item.value"></el-option>
          </template>
        </el-select>
      </el-form-item>
      <el-form-item size="small" label="域类型" v-if="deviceModel =='jv210'">
        <el-select v-model="extraParam.region" placeholder="请选择域类型" style="width: 130px;">
          <el-option v-for="item in regionOption" :key="item.uuid" :label="item.stationName" :value="item.identity">
        </el-select>
      </el-form-item>
      <!-- <el-form-item size="small" label="设备IP" prop="deviceIp">
        <el-input v-model="deviceIp" style="width: 200px;" placeholder="输入新的设备IP"></el-input>
      </el-form-item> -->

      <el-form-item size="small" label="地点" prop="bundleAlias">
        <el-input v-model="location" style="width: 200px;"></el-input>
      </el-form-item>
      <el-form-item size="small" label="接入层" v-if="deviceModel =='jv210'" prop="accessNodeName">
        <el-input v-model="accessNodeName" style="width: 200px;" readOnly></el-input>
      </el-form-item>
      <!-- <el-form-item size="small" label="设备端口" prop="devicePort">
        <el-input v-model="devicePort" style="width: 200px;" placeholder="输入新的设备端口"></el-input>
      </el-form-item> -->
      <el-form-item size="small" label="源组播Ip" v-if="isFictitiouVisable">
        <el-input v-model="multicastSourceIp" style="width: 200px;"></el-input>
      </el-form-item>
      <el-form-item size="small" label="编码组播地址" v-show="isFictitiouVisable">
        <el-input v-model="multicastEncodeAddr" style="width: 200px;"></el-input>
      </el-form-item>

      <!-- <el-form-item size="small" label="解码组播">
        <el-switch v-model="multicastDecode" active-color="#13ce66" inactive-color="#ff4949"></el-switch>
      </el-form-item>
      <el-form-item size="small" label="是否转码">
        <el-switch v-model="transcod" active-color="#13ce66" inactive-color="#ff4949"></el-switch>
      </el-form-item> -->
      <el-form-item size="small" label="编码组播" v-show="isFictitiouVisable">
        <el-switch v-model="multicastEncode" active-color="#13ce66" inactive-color="#ff4949"></el-switch>
      </el-form-item>

      <!--
          <div style="margin-top:10px;">
            <el-input v-model="bundleName" placeholder="输入新的设备名称" style="width: 300px;">
              <template slot="prepend">设备名称</template>
            </el-input>
          </div>
          -->
      <el-form-item size="small" label="设备分组" prop="bundleFolderName">
        <el-input v-model="bundleFolderName" style="width: 200px;" readOnly @click.native="handleChangeFolder"></el-input>
      </el-form-item>

    </el-form>
    <el-card class="box-card" style="margin-top:10px">
      <div slot="header" class="clearfix">
        <span>设备参数设置</span>
      </div>
      <div class="bundleConfig" v-show="cardVisable">
        <div v-show="extraParam.dev_type == 'dh_camera'">
          <el-divider content-position="left">编码参数</el-divider>
          <el-row :gutter="6">
            <el-form ref="dahuaForm" :model="dahuaFormData" :rules="dahuaRules" size="mini" label-width="140px">
              <el-col :span="21">
                <el-form-item label="url" prop="url">
                  <el-input v-model="dahuaFormData.url" placeholder="请输入url" clearable :style="{width: '100%'}">
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="21">
                <el-form-item label="模式设置" prop="url">
                  <el-select v-model="modelConfig" placeholder="请选择" style="width: 100%;" @change="encodeModeChange">
                    <el-option label="质量优先" value="quality_first"></el-option>
                    <el-option label="码率优先" value="bitrate_first"></el-option>
                    <el-option label="自定义" value="other"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="21">
                <el-form-item label="字幕大小（px）" prop="osd_font_size">
                  <el-select v-model.number="dahuaFormData.osd_font_size" placeholder="请选择" style="width: 100%;">
                    <el-option label="16*16" :value="16"></el-option>
                    <el-option label="32*32" :value="32"></el-option>
                    <el-option label="48*48" :value="48"></el-option>
                    <el-option label="64*64" :value="64"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="编码方式" prop="enc_param.br_ctrl">
                  <el-select v-model="dahuaFormData.enc_param.br_ctrl" placeholder="请选择" style="width: 100%;">
                    <el-option label="固定码率" value="cbr"></el-option>
                    <el-option label="可变码率" value="vbr"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="编码类型" prop="enc_param.codec">
                  <el-select v-model="dahuaFormData.enc_param.codec" placeholder="请选择" style="width: 100%;">
                    <el-option label="H264" value="h264"></el-option>
                    <el-option label="H265" value="h265"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="编码质量" prop="enc_param.quality">
                  <el-select v-model="dahuaFormData.enc_param.quality" placeholder="请选择" style="width: 100%;">
                    <el-option label="10" :value="10"></el-option>
                    <el-option label="30" :value="30"></el-option>
                    <el-option label="50" :value="50"></el-option>
                    <el-option label="60" :value="60"></el-option>
                    <el-option label="80" :value="80"></el-option>
                    <el-option label="100" :value="100"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="编码码率" prop="enc_param.bitrate">
                  <el-input :disabled="bitrateDisable" v-model.number="dahuaFormData.enc_param.bitrate" placeholder="请输入编码码率" clearable :style="{width: '100%'}"><template slot="append">kbps</template></el-input>

                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="视频宽度" prop="enc_param.width">
                  <el-input v-model.number="dahuaFormData.enc_param.width" placeholder="请输入width" clearable :style="{width: '100%'}">
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="视频帧率" prop="enc_param.frame_rate">
                  <el-input v-model.number="dahuaFormData.enc_param.frame_rate" placeholder="请输入frame_rate" clearable :style="{width: '100%'}"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="视频高度" prop="enc_param.height">
                  <el-input v-model.number="dahuaFormData.enc_param.height" placeholder="请输入height" clearable :style="{width: '100%'}"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="视频I帧间隔" prop="enc_param.iframe_interva">
                  <el-input v-model.number="dahuaFormData.enc_param.iframe_interva" placeholder="请输入视频I帧间隔" clearable :style="{width: '100%'}"></el-input>
                </el-form-item>
              </el-col>

              <el-col :span="24">
                <el-divider content-position="left">标题字幕</el-divider>
                <el-row>
                  <el-col :span="7">
                    <el-form-item label="启用" prop="text_osd.enable">
                      <el-switch v-model="dahuaFormData.text_osd.enable" active-color="#13ce66" inactive-color="#ff4949"></el-switch>
                    </el-form-item>
                  </el-col>
                  <el-col :span="7">
                    <el-form-item label="标题内容" prop="text_osd.content">
                      <el-input v-model="dahuaFormData.text_osd.content" placeholder="请输入标题内容" clearable :style="{width: '100%'}"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="7">
                    <el-form-item label="字幕颜色" prop="text_osd.color" required>
                      <el-color-picker v-model="dahuaFormData.text_osd.color" size="medium"></el-color-picker>
                    </el-form-item>
                  </el-col>

                  <el-col :span="7">
                    <el-form-item label="横坐标" prop="text_osd.x">
                      <el-input v-model.number="dahuaFormData.text_osd.x" placeholder="请输入text_osd_x" clearable :style="{width: '100%'}">
                        <template slot="append">[0-8192]</template>
                      </el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="7">
                    <el-form-item label="纵坐标" prop="text_osd.y">
                      <el-input v-model.number="dahuaFormData.text_osd.y" placeholder="请输入text_osd_y" clearable :style="{width: '100%'}">
                        <template slot="append">[0-8192]</template>
                      </el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
              </el-col>

              <el-col :span="24">
                <el-divider content-position="left">日期字幕</el-divider>
                <el-row>
                  <el-col :span="7">
                    <el-form-item label="是否启用" prop="date_osd.enable">
                      <el-switch v-model="dahuaFormData.date_osd.enable" active-color="#13ce66" inactive-color="#ff4949"></el-switch>
                    </el-form-item>
                  </el-col>
                  <el-col :span="7">
                    <el-form-item label="启用星期" prop="date_osd.has_week">
                      <el-switch v-model="dahuaFormData.date_osd.has_week" active-color="#13ce66" inactive-color="#ff4949"></el-switch>
                    </el-form-item>
                  </el-col>
                  <el-col :span="7">
                    <el-form-item label="字幕颜色" prop="date_osd.color" required>
                      <el-color-picker v-model="dahuaFormData.date_osd.color" size="medium"></el-color-picker>
                    </el-form-item>
                  </el-col>
                  <el-col :span="7">
                    <el-form-item label="横坐标" prop="date_osd.x">
                      <el-input v-model.number="dahuaFormData.date_osd.x" placeholder="请输入date_osd_x" clearable :style="{width: '100%'}"><template slot="append">[0-8192]</template></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="7">
                    <el-form-item label="纵坐标" prop="date_osd.y">
                      <el-input v-model.number="dahuaFormData.date_osd.y" placeholder="请输入date_osd_y" clearable :style="{width: '100%'}"><template slot="append">[0-8192]</template></el-input>
                    </el-form-item>
                  </el-col>

                </el-row>
              </el-col>
            </el-form>
          </el-row>
        </div>
        <div v-show="extraParam.dev_type == 'ts_enc'">
          <el-row :gutter="10">
            <el-form ref="TSencForm" :model="TSencFormData" :rules="TSencRules" size="mini" label-width="135px">
              <el-col :span="7">
                <el-form-item label="接收类型" prop="is_multi">
                  <el-select v-model="TSencFormData.is_multi" placeholder="请选择接收类型" :style="{width: '100%'}" @change="TSencFormIsMultiChange">
                    <el-option v-for="(item, index) in is_multiOptions" :key="index" :label="item.label" :value="item.value" :disabled="item.disabled"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="组播ip" :prop="TSencFormData.is_multi?'multi_ip':''">
                  <el-input v-model="TSencFormData.multi_ip" placeholder="请输入组播ip" clearable :style="{width: '100%'}" :disabled="TSencFormMultiIpDis"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="本机ip" :prop="TSencFormData.is_multi?'local_ip':''">
                  <el-input v-model="TSencFormData.local_ip" placeholder="请输入本机ip" clearable :style="{width: '100%'}"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="接收端口" prop="port">
                  <el-input v-model.number="TSencFormData.port" placeholder="请输入接收端口" clearable :style="{width: '100%'}"></el-input>
                </el-form-item>
              </el-col>
            </el-form>
          </el-row>
        </div>
        <div v-show="extraParam.dev_type == 'ts_dec'">
          <el-row :gutter="10">
            <el-form ref="TSdecForm" :model="TSdecFormData" :rules="TSdecRules" size="mini" label-width="135px">
              <el-col :span="7">
                <el-form-item label="目标ip" prop="dest_ip">
                  <el-input v-model="TSdecFormData.dest_ip" placeholder="请输入目标ip" clearable :style="{width: '100%'}"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="本机ip" prop="local_ip">
                  <el-input v-model="TSdecFormData.local_ip" placeholder="请输入本机ip" clearable :style="{width: '100%'}"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="目标端口" prop="dest_port">
                  <el-input v-model.number="TSdecFormData.dest_port" placeholder="请输入目标端口" clearable :style="{width: '100%'}"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="重校对时间戳" prop="reset_tm">
                  <el-select v-model="TSdecFormData.reset_tm" placeholder="请选择重校对时间戳" :style="{width: '100%'}">
                    <el-option v-for="(item, index) in reset_tmOptions" :key="index" :label="item.label" :value="item.value" :disabled="item.disabled"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-form>
          </el-row>
        </div>
        <div v-show="extraParam.dev_type == 'rtp_passby_enc'">
          <el-row :gutter="10">
            <el-form ref="rtpPassbyEncForm" :model="rtpPassbyEncFormData" :rules="rtpPassbyEncRules" size="mini" label-width="135px">
              <el-col :span="7">
                <el-form-item label="接收类型" prop="is_multi">
                  <el-select v-model="rtpPassbyEncFormData.is_multi" placeholder="请选择接收类型" :style="{width: '100%'}" @change="rtpPassbyEncFormIsMultiChange">
                    <el-option v-for="(item, index) in is_multiOptions" :key="index" :label="item.label" :value="item.value" :disabled="item.disabled"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="组播ip" :prop="rtpPassbyEncFormData.is_multi?'multi_ip':''">
                  <el-input v-model="rtpPassbyEncFormData.multi_ip" placeholder="请输入组播ip" clearable :style="{width: '100%'}" :disabled="rtpPassbyEncFormMultiIpDis"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="本机ip" :prop="rtpPassbyEncFormData.is_multi?'local_ip':''">
                  <el-input v-model="rtpPassbyEncFormData.local_ip" placeholder="请输入本机ip" clearable :style="{width: '100%'}"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="视频接收端口" prop="video_port">
                  <el-input v-model.number="rtpPassbyEncFormData.video_port" placeholder="请输入视频接收端口" clearable :style="{width: '100%'}"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="音频接收端口" prop="audio_port">
                  <el-input v-model.number="rtpPassbyEncFormData.audio_port" placeholder="请输入音频接收端口" clearable :style="{width: '100%'}"></el-input>
                </el-form-item>
              </el-col>
            </el-form>
          </el-row>
        </div>
        <div v-show="extraParam.dev_type == 'rtp_passby_dec'">
          <el-row :gutter="10">
            <el-form ref="rtpPassbyDecForm" :model="rtpPassbyDecFormData" :rules="rtpPassbyDecRules" size="mini" label-width="135px">
              <el-col :span="7">
                <el-form-item label="目标ip" prop="dest_ip">
                  <el-input v-model="rtpPassbyDecFormData.dest_ip" placeholder="请输入目标ip" clearable :style="{width: '100%'}"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="本机ip" prop="local_ip">
                  <el-input v-model="rtpPassbyDecFormData.local_ip" placeholder="请输入本机ip" clearable :style="{width: '100%'}"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="视频接收端口" prop="video_port">
                  <el-input v-model.number="rtpPassbyDecFormData.video_port" placeholder="请输入视频接收端口" clearable :style="{width: '100%'}"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="音频接收端口" prop="audio_port">
                  <el-input v-model.number="rtpPassbyDecFormData.audio_port" placeholder="请输入音频接收端口" clearable :style="{width: '100%'}"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="重校对时间戳" prop="reset_tm">
                  <el-select v-model="rtpPassbyDecFormData.reset_tm" placeholder="请选择重校对时间戳" :style="{width: '100%'}">
                    <el-option v-for="(item, index) in reset_tmOptions" :key="index" :label="item.label" :value="item.value" :disabled="item.disabled"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="强制aac输出" prop="aac_out">
                  <el-select v-model="rtpPassbyDecFormData.aac_out" placeholder="请选择强制aac输出" :style="{width: '100%'}">
                    <el-option v-for="(item, index) in aac_outOptions" :key="index" :label="item.label" :value="item.value" :disabled="item.disabled"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-form>
          </el-row>
        </div>
        <div v-show="extraParam.dev_type == 'rtsp_enc'">
          <el-row :gutter="15">
            <el-form ref="rtspEncForm" :model="rtspEncFormData" :rules="rtspEncRules" size="mini" label-width="135px">
              <el-col :span="12">
                <el-form-item label="流地址" prop="url">
                  <el-input v-model="rtspEncFormData.url" placeholder="rtsp://xxxxx" show-word-limit clearable :style="{width: '100%'}"></el-input>
                </el-form-item>
              </el-col>
            </el-form>
          </el-row>
        </div>
        <div v-show="extraParam.dev_type == 'rtmp_enc'">
          <el-row :gutter="15">
            <el-form ref="rtspEncForm" :model="rtmpEncFormData" :rules="rtmpEncRules" size="mini" label-width="135px">
              <el-col :span="12">
                <el-form-item label="流地址" prop="url">
                  <el-input v-model="rtmpEncFormData.url" placeholder="rtmp://xxxxx" show-word-limit clearable :style="{width: '100%'}"></el-input>
                </el-form-item>
              </el-col>
            </el-form>
          </el-row>
        </div>
        <div v-show="extraParam.dev_type == 'onvif_enc'">
          <el-row :gutter="10">
            <el-form ref="onvifEncForm" :model="onvifEncFormData" :rules="onvifEncRules" size="mini" label-width="135px">
              <el-col :span="7">
                <el-form-item label="用户名" prop="onvif_user">
                  <el-input v-model="onvifEncFormData.onvif_user" placeholder="请输入用户名" clearable :style="{width: '100%'}"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="密码" prop="onvif_pwd">
                  <el-input v-model="onvifEncFormData.onvif_pwd" placeholder="请输入密码" clearable :style="{width: '100%'}"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="控制IP" prop="onvif_ip">
                  <el-input v-model="onvifEncFormData.onvif_ip" placeholder="请输入控制IP" clearable :style="{width: '100%'}"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="控制端口" prop="onvif_port">
                  <el-input v-model.number="onvifEncFormData.onvif_port" placeholder="请输入控制端口" clearable :style="{width: '100%'}"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="选择码率索引" prop="onvif_sel_index">
                  <el-input v-model.number="onvifEncFormData.onvif_sel_index" placeholder="请输入选择码率索引" clearable :style="{width: '100%'}"></el-input>
                </el-form-item>
              </el-col>

            </el-form>
          </el-row>
        </div>
        <div v-show="extraParam.dev_type == 'bq_enc'">
          <el-row :gutter="15">
            <el-form ref="bqEncForm" :model="bqEncFormData" :rules="bqEncRules" size="mini" label-width="135px">
              <el-col :span="7">
                <el-form-item label="设备类型" prop="bq_type">
                  <el-select v-model="bqEncFormData.bq_type" placeholder="请选择设备类型" :style="{width: '100%'}">
                    <el-option v-for="(item, index) in bq_typeOptions" :key="index" :label="item.label" :value="item.value" :disabled="item.disabled"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="15">
                <el-form-item label="流地址" prop="url">
                  <el-input v-model="bqEncFormData.url" placeholder="请输入流地址" clearable :style="{width: '100%'}">
                  </el-input>
                </el-form-item>
              </el-col>
            </el-form>
          </el-row>
        </div>
        <div v-show="extraParam.dev_type == 'transcode_dec'">
          <el-row :gutter="15">
            <el-form ref="transcodeEecForm" :model="transcodeEecFormData" :rules="transcodeEecRules" size="mini" label-width="135px">
              <el-col :span="7">
                <el-form-item label="编码类型" prop="dst_codec">
                  <el-select v-model="transcodeEecFormData.dst_codec" placeholder="请选择编码类型" :style="{width: '100%'}">
                    <el-option v-for="(item, index) in dst_codecOptions" :key="index" :label="item.label" :value="item.value" :disabled="item.disabled"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="视频宽度" prop="width">
                  <el-input v-model.number="transcodeEecFormData.width" placeholder="请输入视频宽度" clearable :style="{width: '100%'}"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="视频高度" prop="height">
                  <el-input v-model.number="transcodeEecFormData.height" placeholder="请输入视频高度" clearable :style="{width: '100%'}"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="视频帧率" prop="fps">
                  <el-input v-model.number="transcodeEecFormData.fps" placeholder="请输入视频帧率" clearable :style="{width: '100%'}"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="视频I帧间隔" prop="gop_size">
                  <el-input v-model.number="transcodeEecFormData.gop_size" placeholder="请输入帧间隔" clearable :style="{width: '100%'}"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="url" prop="url">
                  <el-input v-model.number="transcodeEecFormData.url" placeholder="udp://ip:port@localip" clearable :style="{width: '100%'}"></el-input>
                </el-form-item>
              </el-col>
            </el-form>
          </el-row>
        </div>
      </div>
    </el-card>
    <el-button v-show="false" type="info" size="small" @click="addExtraInfo" style="margin-top:20px; margin-left: 30px">新增扩展字段</el-button>

    <!-- <div v-show="false" style="margin-top:10px;" v-for="(extraInfo, index) in extraInfos">
      <el-input size="small" v-model="extraInfo.name" placeholder="扩展字段名" style="width: 180px;"></el-input>
      <el-input size="small" v-model="extraInfo.value" placeholder="扩展字段值" style="width: 180px;margin-left: 10px;"></el-input>
      <el-button size="small" type="danger" @click.prevent="remove(extraInfo)" style="margin-left: 10px;">删除</el-button>
    </div> -->
    <div style="margin-top:30px; margin-left: 30px">
      <!--<el-button type="info" @click="addExtraInfo">新增扩展字段</el-button>-->
      <el-button size="small" type="primary" @click="submit">提交</el-button>
    </div>
    <!-- 分组弹窗 -->
    <template>
      <el-dialog title="选择分组" :visible.sync="dialog.changeFolder.visible" width="650px" :before-close="handleChangeFolderClose">
        <div style="height:500px; position:relative;">
          <el-scrollbar style="height:100%;">
            <el-tree ref="changeFolderTree" :props="dialog.changeFolder.tree.props" :data="dialog.changeFolder.tree.data" node-key="id" check-strictly :expand-on-click-node="false" default-expand-all highlight-current @current-change="currentTreeNodeChange">

              <span style="flex:1; display:flex; align-items:center; justify-content:space-between; padding-right:8px;" slot-scope="{node, data}">
                <span style="font-size:14px;">
                  <span class="icon-folder-close"></span>
                  <span>{{data.name}}</span>
                </span>
                <span>
                  <el-button type="text" size="mini" style="padding:0;" @click.stop="treeNodeAppend(node, data)">
                    <span style="font-size:16px;" class="icon-plus"></span>
                  </el-button>
                </span>
              </span>

            </el-tree>
          </el-scrollbar>
        </div>
        <span slot="footer" class="dialog-footer">
          <el-button size="medium" @click="handleChangeFolderClose">取消</el-button>
          <el-button size="medium" type="primary" @click="handleChangeFolderCommit">确定</el-button>
        </span>
      </el-dialog>

      <el-dialog title="添加分组" :visible.sync="dialog.addFolder.visible" width="450px" :before-close="handleAddFolderClose">
        <el-form label-width="110px">
          <el-form-item label="分组名称">
            <el-input v-model="dialog.addFolder.folderName"></el-input>
          </el-form-item>
          <el-form-item label="是否同步ldap">
            <el-select v-model="dialog.addFolder.toLdap">
              <el-option key="是" label="是" value="是">
                是
              </el-option>
              <el-option key="否" label="否" value="否">
                否
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button size="medium" @click="handleAddFolderClose">取消</el-button>
          <el-button size="medium" type="primary" @click="handleAddFolderCommit">确定</el-button>
        </span>
      </el-dialog>

    </template>
  </section>
</template>

<script type="text/ecmascript-6">
import { queryBundleExtraInfo, modifyBundleExtraInfo, queryFolderTree, getStationList } from '../../api/api';

export default {
  name: "LwModifyBundle",
  data () {
    return {
      activeTabName: "LwModifyBundle",
      bundleId: this.$route.query.bundleId,
      bundleName: this.$route.query.bundleName,
      location: this.$route.query.location,
      deviceIp: this.$route.query.deviceIp,
      devicePort: this.$route.query.devicePort,
      multicastEncode: this.$route.query.multicastEncode,
      multicastEncodeAddr: this.$route.query.multicastEncodeAddr,
      multicastDecode: this.$route.query.multicastDecode,
      extraInfos: [],
      bundleFolderId: this.$route.query.bundleFolderId,
      bundleFolderName: this.$route.query.bundleFolderName,
      transcod: this.$route.query.transcod,
      multicastSourceIp: this.$route.query.multicastSourceIp,
      accessNodeName: this.$route.query.accessNodeName,
      deviceModel: this.$route.query.deviceModel,
      dialog: {
        changeFolder: {
          visible: false,
          tree: {
            props: {
              label: 'name',
              children: 'children'
            },
            expandOnClickNode: false,
            data: [],
            current: ''
          },
        },
        addFolder: {
          visible: false,
          parent: '',
          parentNode: '',
          folderName: '',
          toLdap: '否'
        },
        /*  */
      },
      devTypeOption: [
        { label: "sip编码器", value: "sip_enc" },
        { label: "sip解码器", value: "sip_dec" },
        { label: "sip编解码器", value: "sip_enc_dec" },
        { label: "大华摄像机", value: "dh_camera" },
        { label: "ts输入(虚编码)", value: "ts_enc" },
        { label: "ts输出(虚解码)", value: "ts_dec" },
        { label: "rtp透传输入(虚编码)", value: "rtp_passby_enc" },
        { label: "rtp透传输出(虚解码)", value: "rtp_passby_dec" },
        { label: "rtsp输入(虚编码)", value: "rtsp_enc" },
        { label: "rtmp输入(虚编码)", value: "rtmp_enc" },
        { label: "onvif输入(虚编码)", value: "onvif_enc" },
        { label: "北清编码器输入", value: "bq_enc" },
        { label: "28181编码器输入", value: "28181_enc" },
        { label: "转码输出(虚解码)", value: "transcode_dec" },
      ],
      params: {},
      extraInfosEdit: [],
      extraParam: {
        dev_type: '',
        region: '',
        address: ''
      },
      deviceModelOptions: [
        { label: "终端设备", value: "jv210" },
        { label: "存储设备", value: "cdn" }
      ],
      regionOption: [
        { name: "本域", key: "self" },
        { name: "外域", key: "external" },
        { name: "卫通", key: "weitong" },
      ],
      modelConfig: 'other',
      dahuaFormData: {
        url: "",
        osd_font_size: undefined,
        enc_param: {
          br_ctrl: undefined,
          codec: undefined,
          quality: undefined,
          bitrate: undefined,
          width: undefined,
          frame_rate: undefined,
          height: undefined,
          iframe_interva: undefined,
        },
        text_osd: {
          enable: undefined,
          content: undefined,
          x: undefined,
          y: undefined,
          color: undefined,
        },
        date_osd: {
          enable: undefined,
          has_week: undefined,
          x: undefined,
          y: undefined,
          color: undefined,
        },
      },
      dahuaRules: {
        url: [{
          required: true,
          message: '请输入url',
          trigger: 'blur'
        }],

        osd_font_size: [{
          required: true,
          message: '请输入osd_font_size',
          trigger: 'blur'
        }],
        'enc_param.br_ctrl': [{
          required: true,
          message: '请输入br_ctrl',
          trigger: 'blur'
        }],
        'enc_param.codec': [{
          required: true,
          message: '请输入codec',
          trigger: 'blur'
        }],
        'enc_param.quality': [{
          required: true,
          message: '请输入quality',
          trigger: 'blur'
        }],
        'enc_param.bitrate': [{
          required: true,
          message: '请输入bitrate',
          trigger: 'blur'
        }],
        'enc_param.width': [{
          required: true,
          message: '请输入width',
          trigger: 'blur'
        }],
        'enc_param.frame_rate': [{
          required: true,
          message: '请输入frame_rate',
          trigger: 'blur'
        }],
        'enc_param.height': [{
          required: true,
          message: '请输入height',
          trigger: 'blur'
        }],
        'enc_param.iframe_interva': [{
          required: true,
          message: '请输入iframe_interva',
          trigger: 'blur'
        }],
        "text_osd.enable": [{
          required: true,
          message: '请输入text_osd_enable',
          trigger: 'blur'
        }],
        "text_osd.content": [{
          required: true,
          message: '请输入text_osd_content',
          trigger: 'blur'
        }],
        "text_osd.x": [{
          required: true,
          message: '请输入text_osd_x',
          trigger: 'blur'
        }],
        "text_osd.y": [{
          required: true,
          message: '请输入text_osd_y',
          trigger: 'blur'
        }],
        "date_osd.enable": [{
          required: true,
          message: '请输入date_osd_enable',
          trigger: 'blur'
        }],
        "date_osd.has_week": [{
          required: true,
          message: '请输入date_osd_has_week',
          trigger: 'blur'
        }],
        "date_osd.x": [{
          required: true,
          message: '请输入date_osd_x',
          trigger: 'blur'
        }],
        "date_osd.y": [{
          required: true,
          message: '请输入date_osd_y',
          trigger: 'blur'
        }],
      },
      cardVisable: false,
      extraInfosAdd: [],
      TSencFormData: {
        is_multi: true,
        multi_ip: "224.1.1.2",
        local_ip: "10.1.41.22",
        port: 2000,
      },
      TSencRules: {
        'is_multi': [{
          required: true,
          message: '请选择接收类型',
          trigger: 'change'
        }],
        'multi_ip': [{
          required: true,
          message: '请输入组播ip',
          trigger: 'blur'
        }],
        'local_ip': [
          {
            required: true,
            message: '请输入本地ip',
            trigger: 'blur'
          }
        ],
        'port': [{
          required: true,
          message: '请输入接收端口',
          trigger: 'blur'
        }]
      },
      is_multiOptions: [{
        "label": "单播",
        "value": false
      }, {
        "label": "组播",
        "value": true
      }],
      TSdecFormData: {
        dest_ip: "224.1.1.2",
        local_ip: "10.1.41.22",
        dest_port: 2000,
        reset_tm: true,
      },
      TSdecRules: {
        dest_ip: [],
        local_ip: [],
        dest_port: [{
          required: true,
          message: '请输入目标端口',
          trigger: 'blur'
        }],
        reset_tm: [{
          required: true,
          message: '请选择重校对时间戳',
          trigger: 'change'
        }],
      },
      reset_tmOptions: [{
        "label": "是",
        "value": true
      }, {
        "label": "否",
        "value": false
      }],
      rtpPassbyDecFormData: {
        dest_ip: "224.1.1.2",
        local_ip: "10.1.41.22",
        video_port: 2000,
        audio_port: 2002,
        reset_tm: false,
        aac_out: false,
      },
      rtpPassbyDecRules: {
        dest_ip: [],
        local_ip: [],
        video_port: [],
        audio_port: [],
        reset_tm: [{
          required: true,
          message: '请选择重校对时间戳',
          trigger: 'change'
        }],
        aac_out: [{
          required: true,
          message: '请选择强制aac输出',
          trigger: 'change'
        }],
      },
      aac_outOptions: [{
        "label": "是",
        "value": true
      }, {
        "label": "否",
        "value": false
      }],
      rtpPassbyEncFormData: {
        is_multi: true,
        multi_ip: "224.1.1.2",
        local_ip: "10.1.41.22",
        video_port: 2000,
        audio_port: 2002,
      },
      rtpPassbyEncRules: {
        is_multi: [{
          required: true,
          message: '请选择接收类型',
          trigger: 'change'
        }],
        'multi_ip': [{
          required: true,
          message: '请输入组播ip',
          trigger: 'blur'
        }],
        'local_ip': [
          {
            required: true,
            message: '请输入本地ip',
            trigger: 'blur'
          }
        ],
        video_port: [],
        audio_port: [],
      },
      rtspEncFormData: {
        url: undefined,
      },
      rtspEncRules: {
        url: [{
          required: true,
          message: '请输入流地址',
          trigger: 'blur'
        }],
      },
      rtmpEncFormData: {
        url: undefined,
      },
      rtmpEncRules: {
        url: [{
          required: true,
          message: '请输入流地址',
          trigger: 'blur'
        }],
      },
      onvifEncFormData: {
        onvif_user: "anonymous",
        onvif_pwd: "anonymous",
        onvif_ip: "10.1.41.223",
        onvif_port: 80,
        onvif_sel_index: 0,
      },
      onvifEncRules: {
        onvif_user: [{
          required: true,
          message: '请输入用户名',
          trigger: 'blur'
        }],
        onvif_pwd: [{
          required: true,
          message: '请输入密码',
          trigger: 'blur'
        }],
        onvif_ip: [],
        onvif_port: [{
          required: true,
          message: '请输入控制端口',
          trigger: 'blur'
        }],
        onvif_sel_index: [{
          required: true,
          message: '请输入选择码率索引',
          trigger: 'blur'
        }],
      },
      bqEncFormData: {
        bq_type: '6931S',
        url: "sstp+udp://127.0.0.1:8002/68ED6661-A641-4A95-9482-96F0AA024424",
      },
      bqEncRules: {
        bq_type: [{
          required: true,
          message: '请选择设备类型',
          trigger: 'change'
        }],
        url: [{
          required: true,
          message: '请输入流地址',
          trigger: 'blur'
        }],
      },
      bq_typeOptions: [{
        "label": "6501S",
        "value": "6501S"
      }, {
        "label": "6601S",
        "value": "6601S"
      }, {
        "label": "6931S",
        "value": "6931S"
      }, {
        "label": "8201C",
        "value": "8201C"
      }, {
        "label": "8361C",
        "value": "8361C"
      }, {
        "label": "8361P",
        "value": "8361P"
      }, {
        "label": "8601C",
        "value": "8601C"
      }, {
        "label": "ts_stream",
        "value": "ts_stream"
      },],
      transcodeEecFormData: {
        dst_codec: "h264",
        width: 720,
        height: 576,
        fps: 25,
        gop_size: 25,
        url: ''
      },
      transcodeEecRules: {
        dst_codec: [{
          required: true,
          message: '请选择编码类型',
          trigger: 'change'
        }],
        width: [{
          required: true,
          message: '请输入视频宽度',
          trigger: 'blur'
        }],
        height: [{
          required: true,
          message: '请输入视频高度',
          trigger: 'blur'
        }],
        fps: [{
          required: true,
          message: '请输入视频帧率',
          trigger: 'blur'
        }],
        gop_size: [{
          required: true,
          message: '请输入视频I帧间隔',
          trigger: 'blur'
        }],
        url: [{
          required: true,
          message: '请输入url',
          trigger: 'blur'
        }],
      },
      dst_codecOptions: [{
        "label": "h264",
        "value": "h264"
      }, {
        "label": "h265",
        "value": "h265"
      }],
      TSencFormMultiIpDis: false,
      rtpPassbyEncFormMultiIpDis: false,
      isFictitiouVisable: true,
      bitrateDisable: false,
      configChannels: [
        { "channelTemplateID": 1, "channelCnt": 1, "channelName": "VenusAudioIn" },
        { "channelTemplateID": 2, "channelCnt": 0, "channelName": "VenusAudioOut" },
        { "channelTemplateID": 3, "channelCnt": 1, "channelName": "VenusVideoIn" },
        { "channelTemplateID": 4, "channelCnt": 0, "channelName": "VenusVideoOut" }
      ],

    };
  },
  methods: {
    handleTabClick (tab, event) {
      if ("LwModifyBundle" !== tab.name) {
        this.$router.push('/' + tab.name);
      }
    },
    queryBundleExtraInfo: function () {
      var self = this;
      let param = {
        bundleId: this.bundleId
      };

      queryBundleExtraInfo(param).then(res => {
        if (res.errMsg) {
          this.$message({
            message: res.errMsg,
            type: 'error'
          });
        } else {
          var extraInfos = res.extraInfos
          for (let i = 0; i < extraInfos.length; i++) {
            if (extraInfos[i].name == "extend_param") {
              var param = JSON.parse(extraInfos[i].value)
              self.params = param.param;
              self.extraParam.dev_type = param.dev_type;
              self.extraParam.region = param.region
              self.location = param.address
              self.extraInfos = extraInfos[i];
            }
          }
          var hidArr = ['sip_enc', 'sip_dec', 'sip_enc_dec', '28181_enc']
          if (hidArr.indexOf(param.dev_type) > -1) {
            self.cardVisable = false;
          } else {
            self.cardVisable = true;

          }

          var isFictitiousArr = ['ts_dec', 'rtp_passby_dec', 'transcode_dec'],
            isEncArr = ['sip_enc', 'dh_camera', 'ts_enc', 'rtp_passby_enc', 'rtsp_enc', 'rtmp_enc', 'onvif_enc', 'bq_enc', '28181_enc']
          if (isEncArr.indexOf(param.dev_type) > -1) {
            this.isFictitiouVisable = true
          } else {
            this.isFictitiouVisable = false
          }
          if (this.deviceModel == "cdn") {
            this.isFictitiouVisable = false;
            this.bundleFolderVisable = false;
          }
          switch (self.extraParam.dev_type) {
            case 'dh_camera':
              self.dahuaFormData = self.params
              break
            case 'ts_enc':
              self.TSencFormData = self.params
              break
            case 'ts_dec':
              self.TSdecFormData = self.params
              break
            case 'rtp_passby_dec':
              self.rtpPassbyDecFormData = self.params
              break
            case 'rtp_passby_enc':
              self.rtpPassbyEncFormData = self.params
              break
            case 'rtsp_enc':
              self.rtspEncFormData = self.params
              break
            case 'rtmp_enc':
              self.rtmpEncFormData = self.params
              break
            case 'onvif_enc':
              self.onvifEncFormData = self.params
              break
            case 'bq_enc':
              self.bqEncFormData = self.params
              break
            case 'transcode_dec':
              self.transcodeEecFormData = self.params
              break
          }
        }
      });
    },
    devTypeChange: function (val) {
      var hidArr = ['sip_enc', 'sip_dec', 'sip_enc_dec', '28181_enc'],
        isEncArr = ['sip_enc', 'dh_camera', 'ts_enc', 'rtp_passby_enc', 'rtsp_enc', 'rtmp_enc', 'onvif_enc', 'bq_enc', '28181_enc']
      if (hidArr.indexOf(val) > -1) {
        debugger
        this.cardVisable = false;
      } else {
        this.cardVisable = true;

      }
      var isFictitiousArr = ['ts_dec', 'rtp_passby_dec', 'transcode_dec']
      if (isFictitiousArr.indexOf(val) > -1) {
        this.isFictitiouVisable = false
      } else {
        this.isFictitiouVisable = true
      }
      // 判断解码编码设备
      if (isEncArr.indexOf(val) > -1) {
        this.bundleForm.coderType = 'ENCODER'
        this.configChannels = [
          { "channelTemplateID": 1, "channelCnt": 1, "channelName": "VenusAudioIn" },
          { "channelTemplateID": 2, "channelCnt": 0, "channelName": "VenusAudioOut" },
          { "channelTemplateID": 3, "channelCnt": 1, "channelName": "VenusVideoIn" },
          { "channelTemplateID": 4, "channelCnt": 0, "channelName": "VenusVideoOut" }
        ]
      } else {
        this.bundleForm.coderType = 'DECODER'
        this.configChannels = [
          { "channelTemplateID": 1, "channelCnt": 0, "channelName": "VenusAudioIn" },
          { "channelTemplateID": 2, "channelCnt": 1, "channelName": "VenusAudioOut" },
          { "channelTemplateID": 3, "channelCnt": 0, "channelName": "VenusVideoIn" },
          { "channelTemplateID": 4, "channelCnt": 1, "channelName": "VenusVideoOut" }
        ]
      }
    },
    addExtraInfo: function () {
      this.extraInfos.push({});
    },
    remove: function (item) {
      var index1 = this.extraInfos.indexOf(item);
      if (index1 !== -1) {
        this.extraInfos.splice(index1, 1);
      }
    },
    submit: function () {
      var self = this;
      // if (!this.validateExtraInfo()) {
      //   return;
      // }
      // var newArr = [];
      var extraParam = this.extraParam;
      extraParam.address = this.location
      switch (self.extraParam.dev_type) {
        case 'dh_camera':
          extraParam.param = this.dahuaFormData
          break
        case 'sip_enc':
          extraParam.param = {}
          break
        case 'sip_dec':
          extraParam.param = {}
          break
        case 'sip_enc_dec':
          extraParam.param = {}
          break
        case '28181_enc':
          extraParam.param = {}
          break
        case 'ts_enc':
          extraParam.param = this.TSencFormData
          break
        case 'ts_dec':
          extraParam.param = this.TSdecFormData
          break
        case 'rtp_passby_dec':
          extraParam.param = this.rtpPassbyDecFormData
          breakcase
        case 'rtp_passby_enc':
          extraParam.param = this.rtpPassbyEncFormData
          break
        case 'rtsp_enc':
          extraParam.param = this.rtspEncFormData
          break
        case 'rtmp_enc':
          extraParam.param = this.rtmpEncFormData
          break
        case 'onvif_enc':
          extraParam.param = this.onvifEncFormData
          break
        case 'bq_enc':
          extraParam.param = this.bqEncFormData
          break
        case 'transcode_dec':
          extraParam.param = this.transcodeEecFormData
          break
      }

      this.extraInfos.value = extraParam

      let param = {
        bundleId: this.bundleId,
        bundleName: this.bundleName,
        location: this.location,
        folderId: this.bundleFolderId,
        deviceIp: this.deviceIp,
        devicePort: this.devicePort,
        multicastEncode: this.multicastEncode,
        multicastEncodeAddr: this.multicastEncodeAddr,
        multicastDecode: this.multicastDecode,
        transcod: this.transcod,
        multicastSourceIp: this.multicastSourceIp,
        extraInfos: JSON.stringify([this.extraInfos])
      };

      modifyBundleExtraInfo(param).then(res => {
        if (res.errMsg) {
          this.$message({
            message: res.errMsg,
            type: 'error'
          });
        } else {
          this.$message({
            message: "修改成功",
            type: 'success'
          });

          this.handleConfigBundle(res.bundleId)
        }
      });
    },
    validateExtraInfo: function () {
      // for (let extraInfo of this.extraInfos) {
      //   if (!extraInfo.name || !extraInfo.value) {
      //     this.$message({
      //       message: "扩展字段不能为空",
      //       type: 'error'
      //     });
      //     return false;
      //   }
      // }
      // return true;
    },
    handleConfigBundle: function (bundleId) {
      let param = {
        bundleId: bundleId,
        configChannels: JSON.stringify(this.configChannels),
        configEditableAttrs: JSON.stringify([])
      };

      configBundle(param).then(res => {
        // if (res.errMsg) {
        //   this.$message({
        //     message: res.errMsg,
        //     type: 'error'
        //   });
        // } else {
        //   this.$message({
        //     message: "配置成功",
        //     type: 'success'
        //   });
        // }
      });
    },
    // 修改分组方法
    handleChangeFolder: function () {
      var self = this;
      self.dialog.changeFolder.visible = true;
      queryFolderTree().then(res => {
        if (res.status === 200) {
          self.dialog.changeFolder.tree.data.splice(0, self.dialog.changeFolder.tree.data.length);
          if (res.data && res.data.length > 0) {
            for (var i = 0; i < res.data.length; i++) {
              self.dialog.changeFolder.tree.data.push(res.data[i]);
            }
          }
        } else {
          self.$message({
            type: 'error',
            message: res.message
          });
        }
      });
    },
    handleChangeFolderClose: function () {
      var self = this;
      self.dialog.changeFolder.visible = false;
      self.dialog.changeFolder.tree.data.splice(0, self.dialog.changeFolder.tree.data.length);
      self.dialog.changeFolder.tree.current = '';
    },
    handleChangeFolderCommit: function () {
      var self = this;
      console.log(self.dialog.changeFolder)
      self.bundleFolderId = self.dialog.changeFolder.tree.current.id;
      self.bundleFolderName = self.dialog.changeFolder.tree.current.name;
      self.handleChangeFolderClose();
    },
    currentTreeNodeChange: function (data, node) {
      var self = this;
      self.dialog.changeFolder.tree.current = data;
    },
    treeNodeAppend: function (node, data) {
      var self = this;
      self.dialog.addFolder.parent = data;
      self.dialog.addFolder.parentNode = node;
      self.dialog.addFolder.visible = true;
    },
    handleAddFolderClose: function () {
      var self = this;
      self.dialog.addFolder.parent = '';
      self.dialog.addFolder.parentNode = '';
      self.dialog.addFolder.visible = false;
      self.dialog.addFolder.folderName = '';
      self.dialog.addFolder.toLdap = '否';
    },
    handleAddFolderCommit: function () {
      var self = this;
      addFolder({
        name: self.dialog.addFolder.folderName,
        parentId: self.dialog.addFolder.parent.id,
        toLdap: self.dialog.addFolder.toLdap === '是' ? true : false
      }).then(res => {
        self.dialog.addFolder.parent.children = self.dialog.addFolder.parent.children || [];
        if (!res.errMsg) {
          self.dialog.addFolder.parent.children.push(res.folder);
          self.$refs.changeFolderTree.append(res.folder, self.dialog.addFolder.parentNode);
          self.handleAddFolderClose();
        }
      });
    },
    handleSelectLayerNode: function () {
      var self = this;
      self.$refs.selectLayerNode.show();
    },
    layerNodeSelected: function (layerNode, done) {
      var self = this;
      self.bundleForm.accessNodeUid = layerNode.nodeUid;
      done();
    },
    TSencFormIsMultiChange (val) {
      if (!val) {
        this.TSencFormMultiIpDis = true
      } else {
        this.TSencFormMultiIpDis = false
      }
    },
    rtpPassbyEncFormIsMultiChange (val) {
      if (!val) {
        this.rtpPassbyEncFormMultiIpDis = true
      } else {
        this.rtpPassbyEncFormMultiIpDis = false
      }
    },
    encodeModeChange (val) {
      if (val == "bitrate_first") {
        this.dahuaFormData.enc_param.bitrate = 1024
        this.bitrateDisable = true;
      } else if (val == "quality_first") {
        this.dahuaFormData.enc_param.bitrate = 8192
        this.bitrateDisable = true;
      } else {
        this.bitrateDisable = false;
      }
    }
  },
  mounted () {
    this.queryBundleExtraInfo();
    this.row = JSON.parse(sessionStorage.getItem('row'))
    getStationList().then(res => {
      if (res.errMsg) {
        self.$message({
          message: res.errMsg,
          type: 'error'
        });
      } else {

        self.regionOption = res.data.rows;
        self.regionOption.unshift({
          id: 99999,
          identity: "self",
          stationName: "本域",
        })
      }
    });
  }
}
</script>

<style scoped>
</style>

