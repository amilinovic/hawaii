import styled from 'styled-components';
import { prop } from 'styled-tools';

export const NavigationLink = styled.span`
  color: ${prop('color', 'white')};
  padding: 10px 10px 10px 50px;
  display: ${prop('display', 'inline-block')};
  font-weight: 500;
`;
