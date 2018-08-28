import { call, put } from 'redux-saga/effects';

import {
  requestTeams,
  receiveTeams,
  errorReceivingTeams
} from '../../../store/actions/teamsActions';
import { getAllTeams } from '../../../store/sagas/teamsSaga';
import { getTeamsApi } from '../../../store/services/teamsService';

describe('Get teams saga', () => {
  it('should get teams', () => {
    const mockResults = [
      {
        name: 'TestTeam',
        mails: 'test@mail.com'
      }
    ];
    const iterator = getAllTeams();

    expect(iterator.next().value).toEqual(call(getTeamsApi));

    expect(iterator.next(mockResults).value).toEqual(
      put(receiveTeams(mockResults))
    );
    expect(iterator.next().done).toBe(true);

    it('should handle request errors when getting teams', () => {
      const requestAction = requestTeams();
      const error = new Error('error getting teams');
      const iterator = getAllTeams(requestAction);

      expect(iterator.next().value).toEqual(call(getTeamsApi));
      expect(iterator.throw(error).value).toEqual(
        put(errorReceivingTeams(error))
      );
      expect(iterator.next().done).toBe(true);
    });
  });
});
