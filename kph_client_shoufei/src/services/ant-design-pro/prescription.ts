// @ts-ignore
/* eslint-disable */
import { request } from 'umi';

/** 获取规则列表 GET /api/rule */
export async function queryPrescription(
  params: {
    // query
    /** 当前的页码 */
    current?: number;
    /** 页面的容量 */
    pageSize?: number;
  },
  options?: { [key: string]: any },
) {
  return request<API.PageResult>('/api/prescription/getPrescriptionList', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

export async function getPrescriptionById(
  params: {
    // query
    /** 处方id */
    id?: number;
  },
  options?: { [key: string]: any },
) {
  return request<any>('/api/prescription/getPrescriptionByID', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

// /** 新建规则 PUT /api/rule */
// export async function updateRule(options?: { [key: string]: any }) {
//   return request<API.RuleListItem>('/api/rule', {
//     method: 'PUT',
//     ...(options || {}),
//   });
// }

// /** 新建规则 POST /api/rule */
// export async function addRule(options?: { [key: string]: any }) {
//   return request<API.RuleListItem>('/api/rule', {
//     method: 'POST',
//     ...(options || {}),
//   });
// }

// /** 删除规则 DELETE /api/rule */
// export async function removeRule(options?: { [key: string]: any }) {
//   return request<Record<string, any>>('/api/rule', {
//     method: 'DELETE',
//     ...(options || {}),
//   });
// }
