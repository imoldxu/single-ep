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