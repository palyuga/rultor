/**
 * Copyright (c) 2009-2014, rultor.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the rultor.com nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.rultor.agents;

import com.jcabi.aspects.Immutable;
import com.jcabi.immutable.Array;
import com.jcabi.xml.XML;
import com.rultor.spi.Agent;
import com.rultor.spi.Talk;
import java.io.IOException;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.xembly.Directive;

/**
 * Abstract agent.
 */
@Immutable
@ToString
@EqualsAndHashCode(of = "xpaths")
public abstract class AbstractAgent implements Agent {

    /**
     * Encapsulated XPaths.
     */
    private final transient Array<String> xpaths;

    /**
     * Ctor.
     * @param args XPath expressions
     */
    public AbstractAgent(final String... args) {
        this.xpaths = new Array<String>(args);
    }

    @Override
    public final void execute(final Talk talk) throws IOException {
        final XML xml = talk.read();
        boolean good = true;
        for (final String xpath : this.xpaths) {
            if (xml.nodes(xpath).isEmpty()) {
                good = false;
                break;
            }
        }
        if (good) {
            talk.modify(this.process(xml));
        }
    }

    /**
     * Process it.
     * @param xml Its xml
     * @return Directives
     * @throws java.io.IOException If fails
     */
    protected abstract Iterable<Directive> process(XML xml)
        throws IOException;

}
