// @ts-ignore
/* eslint-disable */
import { request } from 'umi';

/** 获取规则列表 GET /api/rule */
export async function queryDrug(
  params: {
    // query
    /** 当前的页码 */
    current?: number;
    /** 页面的容量 */
    pageSize?: number;
  },
  options?: { [key: string]: any },
) {
  return request<API.PageResult>('/api/drug/getDrugInfoListByKeys', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

export async function getDetail(
  params: {
    // query
    /** 处方id */
    Id?: number;
  },
  options?: { [key: string]: any },
) {
  return request<any>('/api/drug/getDrugByID', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 新建规则 PUT /api/rule */
export async function updateDrug(options?: { [key: string]: any }) {
  return request<API.RuleListItem>('/api/rule', {
    method: 'PUT',
    ...(options || {}),
  });
}

/** 新建规则 POST /api/rule */
export async function addDrug(data:any, options?: { [key: string]: any }) {
  return request<API.RuleListItem>('/api/drug/addDrug', {
    method: 'POST',
    data: data,
    ...(options || {}),
  });
}

/** 新建规则 POST /api/rule */
export async function modifyDrug(data:any, options?: { [key: string]: any }) {
    return request<API.RuleListItem>('/api/drug/modifyDrug', {
      method: 'PUT',
      data: data,
      ...(options || {}),
    });
  }

/** 下架药品 */
export async function downDrug(data:{drugid:number}, options?: { [key: string]: any }) {
  return request<Record<string, any>>('/api/drug/downDrug', {
    method: 'PUT',
    data: data,
    ...(options || {}),
  });
}

/** 上架药品 */
export async function upDrug(data:{drugid:number}, options?: { [key: string]: any }) {
    return request<Record<string, any>>('/api/drug/upDrug', {
      method: 'PUT',
      data: data,
      ...(options || {}),
    });
  }
  