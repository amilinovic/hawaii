import { REQUEST_API_DATA } from './types';
import { RECEIVE_API_DATA } from './types';

export const requestApiData = () => ({
  type: REQUEST_API_DATA
});
export const receiveApiData = data => ({
  type: RECEIVE_API_DATA,
  data
});
