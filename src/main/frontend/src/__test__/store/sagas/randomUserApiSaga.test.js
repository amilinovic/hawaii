import { call, put } from 'redux-saga/effects';

import {
  requestApiData,
  receiveApiData,
  errorReceivingApiData
} from '../../../store/actions/randomUserApiActions';
import { getApiData } from '../../../store/sagas/randomUserApiSaga';
import { randomUserApiRequest } from '../../../store/services/randomUserApiRequest';

describe('get users saga', () => {
  it('should fetch users', () => {
    const mockResults = [
      {
        name: 'someuser',
        url: 'https://randomuser.me/api/'
      }
    ];
    const iterator = getApiData();

    expect(iterator.next().value).toEqual(call(randomUserApiRequest));

    expect(iterator.next(mockResults).value).toEqual(
      put(receiveApiData(mockResults))
    );
    expect(iterator.next().done).toBe(true);

    it('should handle request errors when fetching users', () => {
      const requestAction = requestApiData();
      const error = new Error('error fetching users');
      const iterator = getApiData(requestAction);

      expect(iterator.next().value).toEqual(call(randomUserApiRequest));
      expect(iterator.throw(error).value).toEqual(
        put(errorReceivingApiData(error))
      );
      expect(iterator.next().done).toBe(true);
    });
  });
});
