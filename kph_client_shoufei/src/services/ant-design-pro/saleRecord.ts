// @ts-ignore
/* eslint-disable */
import { request } from 'umi';

/** 获取售药记录 */
export async function querySaleRecord(
  params: {
    /** 当前的页码 */
    current?: number;
    /** 页面的容量 */
    pageSize?: number;
  },
  options?: { [key: string]: any },
) {
  return request<API.PageResult>('/api/saleRecord/querySaleRecord', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

export async function downloadDetail(
  params: any,
  options?: { [key: string]: any },
) {
  return request<any>('/api/saleRecord/excel', {
    method: 'GET',
    params: {
      ...params,
    },
    responseType: 'blob',
    ...(options || {}),
  });
}

export async function statistic(
    params: {
      /** 当前的页码 */
      current?: number;
      /** 页面的容量 */
      pageSize?: number;
    },
    options?: { [key: string]: any },
  ) {
    return request<API.PageResult>('/api/saleRecord/statistic', {
      method: 'GET',
      params: {
        ...params,
      },
      ...(options || {}),
    });
  }

export async function downloadStatistics(
  params: any,
  options?: { [key: string]: any },
) {
  return request<any>('/api/saleRecord/downloadExcel', {
    method: 'GET',
    params: {
      ...params,
    },
    responseType: 'blob',
    ...(options || {}),
  });
}

export async function departStatistic(
  params: {
    /** 当前的页码 */
    current?: number;
    /** 页面的容量 */
    pageSize?: number;
  },
  options?: { [key: string]: any },
) {
  return request<API.PageResult>('/api/saleRecord/department/statistic', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

export async function downloadDepartStatistics(
params: any,
options?: { [key: string]: any },
) {
return request<any>('/api/saleRecord/department/statistic/excel', {
  method: 'GET',
  params: {
    ...params,
  },
  responseType: 'blob',
  ...(options || {}),
});
}
