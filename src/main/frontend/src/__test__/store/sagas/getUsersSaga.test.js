import { call, put } from 'redux-saga/effects';

import {
  requestApiData,
  receiveApiData,
  errorReceivingApiData
} from '../../../store/actions/EmployeesActions';
import { getApiData } from '../../../store/sagas/sagas';
import { fetchData } from '../../../store/services/user';

describe('get users saga', () => {
  it('should fetch users', () => {
    const mockResults = [
      {
        name: 'someuser',
        url: 'https://randomuser.me/api/'
      }
    ];
    const iterator = getApiData();

    expect(iterator.next().value).toEqual(call(fetchData));
    expect(iterator.next(mockResults).value).toEqual(
      put(receiveApiData(mockResults))
    );
    expect(iterator.next().done).toBe(true);

    it('should handle request errors when fetching users', () => {
      const requestAction = requestApiData();
      const error = new Error('error fetching users');
      const iterator = getApiData(requestAction);

      expect(iterator.next().value).toEqual(call(fetchData));
      expect(iterator.throw(error).value).toEqual(
        put(errorReceivingApiData(error))
      );
      expect(iterator.next().done).toBe(true);
    });
  });
});
