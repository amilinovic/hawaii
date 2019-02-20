import { getFactory } from './request';

export const getPublicHolidaysApi = getFactory('/publicholidays?deleted=false');
