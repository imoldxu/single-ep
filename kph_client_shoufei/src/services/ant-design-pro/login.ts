// @ts-ignore
/* eslint-disable */
import { request } from 'umi';

/** 发送验证码 POST /api/login/captcha */
// export async function getFakeCaptcha(
//   params: {
//     // query
//     /** 手机号 */
//     phone?: string;
//   },
//   options?: { [key: string]: any },
// ) {
//   return request<API.FakeCaptcha>('/api/login/captcha', {
//     method: 'POST',
//     params: {
//       ...params,
//     },
//     ...(options || {}),
//   });
// }

/** 登录接口 POST /api/login/outLogin */
export async function outLogin(options?: { [key: string]: any }) {
  return request<any>('/api/user/session', {
    method: 'DELETE',
    ...(options || {}),
  });
}

/** 登录接口 POST /api/login/account */
export async function login(body: API.LoginParams, options?: { [key: string]: any }) {
  return request<any>('/api/user/session', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 登录接口 PUT /api/user/modifyPassword */
export async function modifyPassword(body: {oldPassword: string, newPassword: string}, options?: { [key: string]: any }) {
  return request<any>('/api/user/password', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}