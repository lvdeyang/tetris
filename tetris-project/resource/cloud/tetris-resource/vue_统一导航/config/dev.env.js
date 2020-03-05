'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

//重要
const useLocalRouteConst = true

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  USELOCALROUTE : useLocalRouteConst,
  RESOURCE_ROOT: useLocalRouteConst ? '"http://__requestIP__:8093"': '"http://192.165.56.84:8093"',
  USER_ROOT: useLocalRouteConst? '"http://__requestIP__:8093"' : '"http://192.165.56.84:8093"'
})
