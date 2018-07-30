import getTokenReducer from '../../../store/reducers/GetTokenReducer';
import { requestToken } from '../../../store/actions/GetTokenActions';

describe('Get Token Reducer', () => {
  const mockTokenInformation = {
    token: '2d153236-d103-4fa0-a3cb-8f26a14c1c45',
    role: 'HR_MANAGER'
  };

  it('should have default state', () => {
    const state = getTokenReducer(undefined, {});
    expect(state).toEqual({});
  });

  it('should update token', () => {
    const stateWithResults = {
      type: 'RECEIVE_TOKEN',
      token: mockTokenInformation
    };

    const requestAction = requestToken();
    const newState = getTokenReducer(stateWithResults, requestAction);

    expect(newState.token).toBe(mockTokenInformation);
  });
});
