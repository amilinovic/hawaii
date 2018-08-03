import getTokenReducer from '../../../store/reducers/getTokenReducer';
import { RECEIVE_TOKEN } from '../../../store/actions/getTokenActions';

describe('getTokenReducer', () => {
  it('should have initial state', () => {
    const action = {};
    const initialState = {};

    expect(getTokenReducer(undefined, action)).toEqual(initialState);
  });

  it('should update token', () => {
    const action = { type: RECEIVE_TOKEN, payload: 1 };
    const expectedState = 1;

    expect(getTokenReducer(undefined, action)).toEqual(expectedState);
  });
});
