// @ts-ignore
/* eslint-disable */
import { request } from 'umi';

/** 统计 */
export async function statistics(
  params: {
    startTime: string,
    endTime: string,
  },
  options?: { [key: string]: any },
) {
  return request<any>('/api/bill/statistic', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 统计 */
export async function mystatistics(
  params: {
    startTime: string,
    endTime: string,
  },
  options?: { [key: string]: any },
) {
  return request<any>('/api/bill/mystatistic', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

export async function queryMyBill(
  params: {
    current: number,
    pageSize: number,
  },
  options?: { [key: string]: any },
) {
  return request<any>('/api/bill/queryMyBill', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

export async function downloadBill(
  params: any,
  options?: { [key: string]: any },
) {
  return request<any>('/api/bill/downloadBill', {
    method: 'GET',
    params: {
      ...params,
    },
    responseType: 'blob',
    ...(options || {}),
  });
}

export async function downloadMyBill(
  params: any,
  options?: { [key: string]: any },
) {
  return request<any>('/api/bill/downloadMyBill', {
    method: 'GET',
    params: {
      ...params,
    },
    responseType: 'blob',
    ...(options || {}),
  });
}

export async function queryBill(
    params: {
      current: number,
      pageSize: number,
    },
    options?: { [key: string]: any },
  ) {
    return request<any>('/api/bill/queryBill', {
      method: 'GET',
      params: {
        ...params,
      },
      ...(options || {}),
    });
}