import { call, put } from 'redux-saga/effects';
import { push } from 'connected-react-router';

import {
  REQUEST_TOKEN_FROM_STORAGE,
  requestTokenFromStorage,
  receiveTokenFromStorageError
} from '../../../store/actions/getTokenFromSessionStorageActions';
import { RECEIVE_TOKEN } from '../../../store/actions/getTokenActions';
import { authenticate } from '../../../store/sagas/getTokenFromSessionStorageSaga';
import { getTokenFromSessionStorage } from '../../../store/services/getTokenFromSessionStorage';

describe('getTokenFromSessionStorageSaga', () => {
  it('should get token from session storage', () => {
    const tokenFromSession = '2d153236-d103-4fa0-a3cb-8f26a14c1c45';

    const actionType = {
      type: RECEIVE_TOKEN
    };

    const iterator = authenticate();

    const redirect = tokenFromSession ? '/leave' : '/login';

    expect(iterator.next().value).toEqual(call(getTokenFromSessionStorage));
    expect(iterator.next().value).toEqual(put(actionType));
    expect(iterator.next().value).toEqual(put(push(redirect)));

    expect(iterator.next(redirect).value).toEqual(
      put(requestTokenFromStorage(redirect))
    );

    expect(iterator.next().done).toBe(true);
  });
});
