import styled from 'styled-components';
import ifProp from 'styled-tools/dist/cjs/ifProp';

export const MainLogo = styled.div`
  opacity: ${ifProp('isMinimized', '0', '1')};
  font-family: 'Varela Round';
  background-color: transparent;
  color: white;
  height: 100px;
  width: 100%;
  font-size: 50px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  transition: 0.2s ease-in-out;
  span {
    display: flex;
    width: 100%;
    flex-direction: row;
    justify-content: center;
    align-items: center;
    img {
      height: 28px;
      margin-right: 10px;
    }
    font-size: 14px;
  }
`;
