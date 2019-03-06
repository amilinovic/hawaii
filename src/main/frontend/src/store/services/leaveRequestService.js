import { postFactory } from './request';

const testObj = {
  user: {
    id: 2
  },
  absence: {
    id: 1
  },
  requestStatus: 'PENDING',
  reason: 'aaaaa',
  days: [
    {
      date: '2019-03-15',
      duration: 'FULL_DAY',
      requestStatus: 'PENDING',
      iconUrl: 'icons/vacation.png',
      absenceType: 'DEDUCTED_LEAVE',
      absenceSubtype: 'ANNUAL'
    }
  ],
  currentlyApprovedBy: []
};

export const createLeaveRequestApi = postFactory('/requests', testObj);
