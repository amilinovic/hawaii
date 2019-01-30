import styled from "styled-components";
import {prop, switchProp} from "styled-tools";

export const TeamCalendarWrapper = styled.div``;

export const TeamCalendarContainer = styled.div`
  display: flex;
  width: 100%;
`;

export const TeamCalendarGridContainer = styled.div`
  margin-left:25px;
  th {
    border:1px solid grey;
    background:lightgrey;
    padding:4px;
  }
`;

export const TeamDaysTable = styled.table``;

export const TeamTotalsTable = styled.table`
  width: 225px;
`;

export const TeamCalendarMenus = styled.div`
  height: 50px;
  padding-left: 25px;
  margin-top: 20px;
  margin-bottom: 25px;
`;

export const TeamUsersContainer = styled.div`
  width: 225px;
  padding-top: 35px;
`;

export const TeamCalendarUser = styled.div`
  text-overflow: clip;
  width: 100%;
  height: 40px;
  margin-bottom: 10px;
  padding-left: 5px;
`;

export const TeamCalendarUserImage = styled.div`
  width: 40px;
  height: 40px;
  background-image: url('/users/image/${prop('uId')}');
  background-size: 100%;
  margin-bottom: 10px;
  border-radius: 20px;
  display: inline-box;
`;

export const TeamCalendarUserName = styled.div`
  font-size: 13px;
  vertical-align: top;
  margin-top: 13px;
  margin-left: 10px;
  display: inline-box;
  text-overflow: clip;
`;

export const TeamCalendarUserTh = styled.th`
  width: 28px;
  text-align: center;
  vertical-align: middle;
  height: 35px;
  font-size:14px;
`;

export const TeamCalendarUserTr = styled.tr``;

export const TeamCalendarUserTd = styled.td`
  vertical-align: middle;
  text-align: center;
  font-size: 13px;
  height: 50px;
  padding-bottom: 10px;
`;
export const TeamCalendarDay = styled.div`
  background: ${switchProp(prop('weekDay'), {
    S: 'lightgrey'
})};
  background: ${switchProp(prop('requestStatus'), {
    APPROVED: 'lightgreen',
    PENDING: 'orange'
})};
  height: 25px;
  vertical-align: middle;
  display: flex;
  align-items: center;
  justify-content: center;
  img {
    width: 15px;
  }
`;

export const TeamCalendarTotalsTh = styled.th`
  font-size: 10px;
  height: 35px;
  text-align: center;
  vertical-align: middle;
  width: 55px;
`;

export const TeamCalendarTotalsTd = styled.td`
  font-size: 13px;
  height: 50px;
  vertical-align: middle;
  text-align: center;
  padding-bottom: 10px;
`;

export const TeamCalendarViewButton = styled.button`
  margin: 5px;
  padding: 10px;
  padding-left: 10px;
  border-radius: 5px;
  background: red;
  color: white;
  padding-left: 30px;
`;

export const TeamCalendarNavButton = styled.button`
  margin: 5px;
  padding: 10px;
  padding-left: 10px;
  border-radius: 5px;
  background: whitesmoke;
`;

export const TeamCalendarNavSelect = styled.select`
  margin-left: -2px;
  margin-right: -2px;
`;

export const TeamCalendarGridTd = styled.td`
  height: 150px;
  width: 160px;
  border: 1px solid grey;
  vertical-align: top;
  font-size:14px;
  img{
    width:15px;
  }
  >div:first-child{
    background: whitesmoke;
    padding: 2px;
    margin-bottom: 10px;
  }
`;

export const GridLeaveImage = styled.div`
  background: ${switchProp(prop('requestStatus'), {
    APPROVED: 'lightgreen',
    PENDING: 'orange'
})};
  display: inline-flex;
  padding: 2px;
  
`;