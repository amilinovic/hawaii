import { getFactory } from './request';

export const getAllowanceApi = year => getFactory('/allowances/me', { year });
