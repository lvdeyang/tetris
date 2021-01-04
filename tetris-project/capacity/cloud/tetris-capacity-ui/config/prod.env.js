'use strict'

// 重要
const useLocalRouteConst = true


module.exports = {
  NODE_ENV: '"production"',
  USELOCALROUTE : useLocalRouteConst,
  // RESOURCE_ROOT: useLocalRouteConst? '"https://__requestIP__:8743"': '"https://__applicationIP__:8743"',
  // USER_ROOT: useLocalRouteConst? '"https://__requestIP__:8543"' :  '"https://__applicationIP__:8543"'
  CAPACITY_ROOT:  useLocalRouteConst? '"http://__requestIP__:8092"' : '"http://192.165.56.20:8092"',
}
