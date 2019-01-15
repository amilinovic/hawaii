import styled from 'styled-components';
import { ifProp, prop } from 'styled-tools';

export const NavigationLink = styled.span`
  color: ${prop('color', 'white')};
  padding: 10px 10px 10px 50px;
  // background: ${prop('background', '#FA4C50')};
  display: ${prop('display', 'inline-block')};
  font-weight: 500;
`;
