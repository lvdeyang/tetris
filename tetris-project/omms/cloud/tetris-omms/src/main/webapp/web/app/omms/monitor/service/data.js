var data = [{
  id:'level0',
  groups:[{
    id:'spring-eureka',
    types:[{
      id:'eureka',
      name:'注册中心',
      servers:[{
        id:'eureka0',
        ip:'127.0.0.1',
        icon:'icons/5-4.jpg',
        refs:['nginx0', 'zuul0', 'user0', 'user1', 'user2',  'menu0', 'mims0', 'process0', 'cs0', 'cms0', 'p2p0', 'capacity0']
      }, {
        id:'eureka1',
        icon:'icons/5-4.jpg'
      }]
    }],
  },{
    id:'nginx',
    types:[{
      id:'nginx',
      name:'nginx',
      servers:[{
        id:'nginx0',
        icon:'icons/5-4.jpg'
      }, {
        id:'nginx1',
        icon:'icons/5-4.jpg'
      }]
    }]
  }]
}, {
  id:'level1',
  groups:[{
    id:'spring-cloud',
    types:[{
      id:'zuul',
      name:'微服务网关',
      servers:[{
        id:'zuul0',
        icon:'icons/5-4.jpg'
      }]
    }, {
      id:'user',
      name:'用户服务',
      servers:[{
        id:'user0',
        icon:'icons/5-4.jpg'
      }, {
        id:'user1',
        icon:'icons/5-4.jpg'
      }, {
        id:'user2',
        icon:'icons/5-4.jpg'
      }]
    }, {
      id:'menu',
      name:'菜单服务',
      servers:[{
        id:'menu0',
        icon:'icons/5-4.jpg'
      }]
    }, {
      id:'mims',
      name:'媒资服务',
      servers:[{
        id:'mims0',
        icon:'icons/5-4.jpg'
      }]
    }, {
      id:'process',
      name:'流程引擎',
      servers:[{
        id:'process0',
        icon:'icons/5-4.jpg'
      }]
    }, {
      id:'cs',
      name:'轮播服务',
      servers:[{
        id:'cs0',
        icon:'icons/5-4.jpg'
      }]
    }, {
      id:'cms',
      name:'内容管理',
      servers:[{
        id:'cms0',
        icon:'icons/5-4.jpg'
      }]
    }, {
      id:'p2p',
      name:'点对点',
      servers:[{
        id:'p2p0',
        icon:'icons/5-4.jpg'
      }]
    }, {
      id:'capacity',
      name:'能力接入',
      servers:[{
        id:'capacity0',
        icon:'icons/5-4.jpg',
        refs:['ssp0', 'st0', 'record0', 'ft0', 'media0', 'media1', 'media2', 'media3']
      }]
    }]
  }]
}, {
  id:'level2',
  groups:[{
    id:'mss',
    types:[{
      id:'ssp',
      name:'软封装',
      servers:[{
        id:'ssp0',
        icon:'icons/5-4.jpg'
      }]
    }, {
      id:'st',
      name:'流转码',
      servers:[{
        id:'st0',
        icon:'icons/5-4.jpg'
      }]
    }, {
      id:'record',
      name:'收录',
      servers:[{
        id:'record0',
        icon:'icons/5-4.jpg'
      }]
    }, {
      id:'ft',
      name:'文件转码',
      servers:[{
        id:'ft0',
        icon:'icons/5-4.jpg'
      }]
    }, {
      id:'media',
      name:'流媒体',
      servers:[{
        id:'media0',
        icon:'icons/5-4.jpg'
      }, {
        id:'media1',
        icon:'icons/5-4.jpg'
      }, {
        id:'media2',
        icon:'icons/5-4.jpg'
      }, {
        id:'media3',
        icon:'icons/5-4.jpg'
      }]
    }]
  }]
}]