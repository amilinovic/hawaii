import getTokenReducer from '../../../store/reducers/GetTokenReducer';
import { RECEIVE_TOKEN } from '../../../store/actions/GetTokenActions';

describe('GetToken Reducer test', () => {
  it('should return initial state', () => {
    const action = {};
    const initialState = {};

    expect(getTokenReducer(undefined, action)).toEqual(initialState);
  });

  it('should return token', () => {
    const action = { type: RECEIVE_TOKEN, payload: 1 };
    const expectedState = 1;

    expect(getTokenReducer(undefined, action)).toEqual(expectedState);
  });
});
