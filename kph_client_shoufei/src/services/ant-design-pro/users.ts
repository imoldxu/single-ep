// @ts-ignore
/* eslint-disable */
import { request } from 'umi';

/** 登录接口 POST /api/login/outLogin */
export async function queryUser(params:{phone: string, name: string, role: string, state: number}, options?: { [key: string]: any }) {
  return request<any>('/api/user/queryUser', {
    method: 'GET',
    params: params,
    ...(options || {}),
  });
}

/** 登录接口 POST /api/login/account */
export async function register(body: { phone: string, name:string, role:string }, options?: { [key: string]: any }) {
  return request<any>('/api/user', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

export async function validUser(body: { phone: string }, options?: { [key: string]: any }) {
    return request<any>('/api/user/validUser', {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      data: body,
      ...(options || {}),
    });
  }

  export async function invalidUser(body: { phone: string }, options?: { [key: string]: any }) {
    return request<any>('/api/user/invalidUser', {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      data: body,
      ...(options || {}),
    });
  }

/** 登录接口 PUT /api/user/modifyPassword */
export async function resetPassword(body: {oldPassword: string, newPassword: string}, options?: { [key: string]: any }) {
  return request<any>('/api/user/resetPassword', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}