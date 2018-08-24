import { call, put } from 'redux-saga/effects';
import { push } from 'connected-react-router';

import { RECEIVE_TOKEN } from '../../../store/actions/getTokenActions';
import {
  authenticate,
  redirect
} from '../../../store/sagas/getTokenFromSessionStorageSaga';
import { getTokenFromSessionStorage } from '../../../store/services/getTokenFromSessionStorage';

describe('redirectIfTokenExists', () => {
  it('should redirect to leave if token exists', () => {
    const actionType = {
      type: RECEIVE_TOKEN
    };

    const iterator = authenticate();

    expect(iterator.next().value).toEqual(call(getTokenFromSessionStorage));

    const iteratorStep = iterator.next();
    expect(iteratorStep.value).toEqual(put(actionType));
    expect(iteratorStep.done).toBe(false);

    const redirectIteratorLogin = redirect();

    expect(redirectIteratorLogin.next().value).toEqual(put(push('/login')));
    expect(redirectIteratorLogin.next().done).toEqual(true);

    const redirectIteratorLeave = redirect('truthy value');

    expect(redirectIteratorLeave.next().value).toEqual(put(push('/leave')));
    expect(redirectIteratorLeave.next().done).toEqual(true);
  });
});
