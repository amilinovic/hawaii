import styled from 'styled-components';
import { ifProp, prop } from 'styled-tools';

export const NavigationLink = styled.span`
  opacity: ${ifProp('isMinimized', '0', '1')};
  transition: 0.2s ease-in-out;
  color: ${prop('color', 'white')};
  font-weight: 500;
  width: 200px;
`;
