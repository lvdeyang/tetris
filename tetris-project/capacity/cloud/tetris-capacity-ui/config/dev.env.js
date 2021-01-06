'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

//重要
const useLocalRouteConst = false


module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  USELOCALROUTE : useLocalRouteConst,
  CAPACITY_ROOT:  useLocalRouteConst? '"http://__requestIP__:8098"' : '"http://10.10.40.24:8092"'
})
