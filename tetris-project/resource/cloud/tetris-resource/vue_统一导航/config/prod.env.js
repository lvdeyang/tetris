'use strict'

// 重要
const useLocalRouteConst = true

module.exports = {
  NODE_ENV: '"production"',
  USELOCALROUTE : useLocalRouteConst,
  RESOURCE_ROOT: useLocalRouteConst? '"http://__requestIP__:8093"': '"http://192.165.56.131:8093"',
  USER_ROOT: useLocalRouteConst? '"http://__requestIP__:8093"' :  '"http://192.165.56.131:8093"'
}
