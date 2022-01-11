require('dotenv').config()
const _ = require('lodash')

/**
 * Nếu không có biến môi trường thì trả về default
 * @param env
 * @param _default
 * @returns {*}
 */
function get(env, _default) {
  _default = _default || ''
  const varEnv = _.get(process.env, env, _default)
  return varEnv
}

/**
 * Nếu không có biến môi trường thì trả về Error
 * @param env
 * @returns {*}
 */
function getOrFail(env) {
  const varEnv = _.get(process.env, env)
  if (varEnv) return varEnv
  throw new Error('Thiếu biến môi trường ' + env)
}
module.exports = {
  get,
  getOrFail,
}
