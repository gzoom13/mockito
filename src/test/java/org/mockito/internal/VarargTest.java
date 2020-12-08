package org.mockito.internal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockitousage.IMethods;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class VarargTest {

    @Mock
    private IMethods mock;

    @Test
    public void shouldMatchVarargWithSingleArgument() {
        class StringVarargMatcher implements ArgumentMatcher<String[]>, VarargMatcher {
            @Override
            public boolean matches(String[] argument) {
                return argument[0].equals("1");
            }
        }

        mock.mixedVarargs(1, "1");

        verify(mock).mixedVarargs(eq(1), argThat(new StringVarargMatcher()));
    }

    @Test
    public void shouldMatchVarargWithNoArguments() {
        class StringVarargMatcher implements ArgumentMatcher<String[]>, VarargMatcher {
            @Override
            public boolean matches(String[] argument) {
                return argument.length == 0;
            }
        }

        mock.mixedVarargs(1);

        verify(mock).mixedVarargs(eq(1), argThat(new StringVarargMatcher()));
    }
}

