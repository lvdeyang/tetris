<<<<<<< HEAD
'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

//重要
const useLocalRouteConst = false

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  USELOCALROUTE : useLocalRouteConst,
  // RESOURCE_ROOT: useLocalRouteConst ? '"https://__requestIP__:8743"': '"http://10.10.40.115:8887"',
  // USER_ROOT: useLocalRouteConst? '"https://__requestIP__:8543"' : '"http://10.10.40.115:8885"'
  ALARM_ROOT:  useLocalRouteConst? '"http://__requestIP__:8097"' : '"http://10.10.40.27:8097"',
  WEBSOCKET_URL : useLocalRouteConst? '"ws://__requestIP__:8097/"': '"ws://10.10.40.27:8097/"'
})
=======
'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

//重要
const useLocalRouteConst = false

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  USELOCALROUTE : useLocalRouteConst,
  // RESOURCE_ROOT: useLocalRouteConst ? '"https://__requestIP__:8743"': '"http://10.10.40.115:8887"',
  // USER_ROOT: useLocalRouteConst? '"https://__requestIP__:8543"' : '"http://10.10.40.115:8885"'
  ALARM_ROOT:  useLocalRouteConst? '"http://__requestIP__:8097"' : '"http://10.10.40.27:8092"',
  WEBSOCKET_URL : useLocalRouteConst? '"ws://__requestIP__:8097/"': '"ws://10.10.40.27:8092/"'
})
>>>>>>> remotes/origin/master
