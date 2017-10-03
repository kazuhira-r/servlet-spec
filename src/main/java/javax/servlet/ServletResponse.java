/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2017 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 *
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 *
 * Copyright 2004 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package javax.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;


/**
 * サーブレットがクライアントにレスポンスを送るのを手助けするオブジェクトを定義します。
 * サーブレットコンテナは<code>ServletResponse</code>オブジェクトを作成し、サーブレットの<code>service</code>メソッドに渡します。
 *
 * <p>レスポンスのMIMEボディでバイナリデータを送るには{@link #getOutputStream}が返す{@link ServletOutputStream}を使用してください。
 * 文字データを送るには{@link #getWriter}が返す<code>PrintWriter</code>オブジェクトを使用してください。
 * バイナリとテキストデータを混在させる、例えば、マルチパートのレスポンスを作成するのであれば<code>ServletOutputStream</code>を使用して文字列の部分は手動で管理します。
 * 
 * <p>レスポンスのMIMEボディの文字コードは以下のような設定で指定することができます。
 * <ul>
 * <li>リクエスト毎
 * <li>ウェブアプリケーション毎({@link ServletContext#setRequestCharacterEncoding}やデプロイメントディスクリプタを使用)
 * <li>コンテナ毎(ベンダー固有の設定を使用してコンテナにデプロイされたすべてのアプリケーションに適用)
 * </ul>
 * 先に述べられた設定のうち複数が使用された場合、プライオリティは記述された順序どおりです。
 * 
 * リクエストごとに、レスポンスの文字コードは{@link #setCharacterEncoding}および{@link #setContentType} メソッドを使用して明示的に指定するか、
 * 暗黙的に{@link #setLocale}メソッドを使用して指定できます。
 * 明示的(explicit)な指定が暗黙(implicit)の指定よりも優先されます。
 * 文字セットが明示的に指定されていない場合はISO-8859-1が使用されます。
 *  
 * <code>setCharacterEncoding</code>や<code>setContentType</code>、<code>setLocale</code>メソッドは
 * <code>getWriter</code>を呼び出す前かつ文字エンコーディングが使用されるレスポンスをコミットする前にs呼び出す必要があります。
 * 
 * <p>MIMEの詳細情報については、<a href="http://www.ietf.org/rfc/rfc2045.txt">RFC 2045</a>などのインターネットRFCを参照してください。
 * SMTPやHTTPなどのプロトコルでMIMEのプロファイルを定義しており、これらの標準はまだ進化し続けています。
 *
 * @author Various
 *
 * @see ServletOutputStream
 */
 
public interface ServletResponse {
    
    /**
     * このレスポンスで送信されるボディに使用されている文字エンコーディング（MIME文字セット）の名前を返します。
     * レスポンスの文字エンコーディングは以下のような方法の降順で指定することができます。
     * <ul>
     * <li>リクエスト毎
     * <li>ウェブアプリケーション毎({@link ServletContext#setRequestCharacterEncoding}やデプロイメントディスクリプタを使用)
     * <li>コンテナ毎(ベンダー固有の設定を使用してコンテナにデプロイされたすべてのアプリケーションに適用)
     * </ul>
     * これらのメソッドのうち最初の結果が返されます。
     * リクエストごとに、レスポンスの文字セットは{@link setCharacterEncoding} および{@link setContentType}メソッドを使用して明示的に指定することも、
     * {@link setLocale}メソッドを暗黙的に使用して明示的に指定することもできます。明示的な指定は暗黙の指定よりも優先されます。
     * <code>getWriter</code>が呼び出された後もしくはレスポンスがコミットされた後にこれらの方法が呼び出されても文字エンコーディングには影響しません。
     * 文字エンコーディングが指定されていない場合は<code>ISO-8859-1</code>が返されます。
     * <p>MIMEと文字エンコーディングの詳細情報については、<a href="http://www.ietf.org/rfc/rfc2047.txt">RFC 2047</a>を参照してください。
     *
     * @return 文字エンコーディングの名前を示す<code>String</code>、例として<code>UTF-8</code>
     */
    public String getCharacterEncoding();
    
    /**
     * レスポンスの中で送られるMIMEボディに使用されるコンテンツタイプを返します。
     * レスポンスがコミットされる前に{@link #setContentType}を使用して適切なコンテンツタイプが指定されていなければいけません。
     * コンテンツタイプが指定されていない場合、このメソッドはnullを返します。
     * コンテンツタイプが指定されており、文字エンコーディングが明示的または{@link #getCharacterEncoding}や{@link #getWriter}が呼び出され暗黙的に指定されている場合は、
     * 返された文字列にcharsetパラメータが含まれます。
     * 文字エンコーディングが指定されていない場合、charsetパラメータは省略されます。
     *
     * @return コンテンツタイプを示す<code>String</code>、例として<code>text/html; charset=UTF-8</code>、もしくはnull
     *
     * @since Servlet 2.4
     */
    public String getContentType();
    
    

    /**
     * レスポンスのバイナリデータを書き出すのに適した{@link ServletOutputStream}を返します。
     * サーブレットコンテナはバイナリデータをエンコードしません。
     *
     * <p>{@link ServletOutputStream#flush()}を呼び出すことでレスポンスをコミットします。
     *
     * このメソッドか{@link #getWriter}はどちらかのみを呼び出すことが出来ます。
     * {@link #reset}メソッドが呼び出されてない限り両方を呼び出すことは出来ません。
     *
     * @return バイナリデータを書き出すための{@link ServletOutputStream} 
     *
     * @exception IllegalStateException このレスポンスで<code>getWriter</code>メソッドがすでに呼び出されている場合
     *
     * @exception IOException I/Oエラーが発生した
     *
     * @see #getWriter
     * @see #reset
     */
    public ServletOutputStream getOutputStream() throws IOException;
    
    /**
     * Returns a <code>PrintWriter</code> object that
     * can send character text to the client.
     * The <code>PrintWriter</code> uses the character
     * encoding returned by {@link #getCharacterEncoding}.
     * If the response's character encoding has not been
     * specified as described in <code>getCharacterEncoding</code>
     * (i.e., the method just returns the default value 
     * <code>ISO-8859-1</code>), <code>getWriter</code>
     * updates it to <code>ISO-8859-1</code>.
     * <p>Calling flush() on the <code>PrintWriter</code>
     * commits the response.
     * <p>Either this method or {@link #getOutputStream} may be called
     * to write the body, not both, except when {@link #reset}
     * has been called.
     * 
     * @return 文字データをクライアントに返すことができる <code>PrintWriter</code>オブジェクト
     *
     * @exception java.io.UnsupportedEncodingException <code>getCharacterEncoding</code>から返された文字エンコーディングが使用できない文字エンコーディングだった
     *
     * @exception IllegalStateException このレスポンスで<code>getOutputStream</code>メソッドがすでに呼び出されている場合
     *
     * @exception IOException I/Oエラーが発生した
     *
     * @see #getOutputStream
     * @see #setCharacterEncoding
     * @see #reset
     */
    public PrintWriter getWriter() throws IOException;
    
    /**
     * Sets the character encoding (MIME charset) of the response
     * being sent to the client, for example, to UTF-8.
     * If the response character encoding has already been set by the
     * {@link ServletContext#setResponseCharacterEncoding},
     * deployment descriptor, or using the setContentType() or setLocale()
     * methods, the value set in this method overrides any of those values.
     * Calling {@link #setContentType} with the <code>String</code>
     * of <code>text/html</code> and calling
     * this method with the <code>String</code> of <code>UTF-8</code>
     * is equivalent with calling
     * <code>setContentType</code> with the <code>String</code> of
     * <code>text/html; charset=UTF-8</code>.
     * <p>This method can be called repeatedly to change the character
     * encoding.
     * This method has no effect if it is called after
     * <code>getWriter</code> has been
     * called or after the response has been committed.
     * <p>Containers must communicate the character encoding used for
     * the servlet response's writer to the client if the protocol
     * provides a way for doing so. In the case of HTTP, the character
     * encoding is communicated as part of the <code>Content-Type</code>
     * header for text media types. Note that the character encoding
     * cannot be communicated via HTTP headers if the servlet does not
     * specify a content type; however, it is still used to encode text
     * written via the servlet response's writer.
     *
     * @param charset IANA 定義の文字セットを示す文字列
     * (http://www.iana.org/assignments/character-sets)
     *
     * @see #setContentType
     * @see #setLocale
     *
     * @since Servlet 2.4
     */
    public void setCharacterEncoding(String charset);
    
    /**
     * Sets the length of the content body in the response
     * In HTTP servlets, this method sets the HTTP Content-Length header.
     *
     * @param len an integer specifying the length of the 
     * content being returned to the client; sets the Content-Length header
     */
    public void setContentLength(int len);
    
    /**
     * Sets the length of the content body in the response
     * In HTTP servlets, this method sets the HTTP Content-Length header.
     *
     * @param len a long specifying the length of the 
     * content being returned to the client; sets the Content-Length header
     *
     * @since Servlet 3.1
     */
    public void setContentLengthLong(long len);

    /**
     * Sets the content type of the response being sent to
     * the client, if the response has not been committed yet.
     * The given content type may include a character encoding
     * specification, for example, <code>text/html;charset=UTF-8</code>.
     * The response's character encoding is only set from the given
     * content type if this method is called before <code>getWriter</code>
     * is called.
     * <p>This method may be called repeatedly to change content type and
     * character encoding.
     * This method has no effect if called after the response
     * has been committed. It does not set the response's character
     * encoding if it is called after <code>getWriter</code>
     * has been called or after the response has been committed.
     * <p>Containers must communicate the content type and the character
     * encoding used for the servlet response's writer to the client if
     * the protocol provides a way for doing so. In the case of HTTP,
     * the <code>Content-Type</code> header is used.
     *
     * @param type コンテンツのMIMEタイプを示す<code>String</code>
     *
     * @see #setLocale
     * @see #setCharacterEncoding
     * @see #getOutputStream
     * @see #getWriter
     *
     */

    public void setContentType(String type);
    

    /**
     * Sets the preferred buffer size for the body of the response.  
     * The servlet container will use a buffer at least as large as 
     * the size requested.  The actual buffer size used can be found
     * using <code>getBufferSize</code>.
     *
     * <p>A larger buffer allows more content to be written before anything is
     * actually sent, thus providing the servlet with more time to set
     * appropriate status codes and headers.  A smaller buffer decreases 
     * server memory load and allows the client to start receiving data more
     * quickly.
     *
     * <p>This method must be called before any response body content is
     * written; if content has been written or the response object has
     * been committed, this method throws an 
     * <code>IllegalStateException</code>.
     *
     * @param size 好ましいバッファーサイズ
     *
     * @exception IllegalStateException コンテンツがすでに書き込まれた後にこのメソッドが呼び出された
     *
     * @see 		#getBufferSize
     * @see 		#flushBuffer
     * @see 		#isCommitted
     * @see 		#reset
     */
    public void setBufferSize(int size);
   
    /**
     * Returns the actual buffer size used for the response.  If no buffering
     * is used, this method returns 0.
     *
     * @return 実際に使用されているバッファーサイズ
     *
     * @see #setBufferSize
     * @see #flushBuffer
     * @see #isCommitted
     * @see #reset
     */
    public int getBufferSize();
    
    /**
     * Forces any content in the buffer to be written to the client.  A call
     * to this method automatically commits the response, meaning the status 
     * code and headers will be written.
     *
     * @see #setBufferSize
     * @see #getBufferSize
     * @see #isCommitted
     * @see #reset

     * @throws IOException バッファーを完全にフラッシュすることができなかった
     *
     */
    public void flushBuffer() throws IOException;
    
    /**
     * Clears the content of the underlying buffer in the response without
     * clearing headers or status code. If the 
     * response has been committed, this method throws an 
     * <code>IllegalStateException</code>.
     *
     * @see #setBufferSize
     * @see #getBufferSize
     * @see #isCommitted
     * @see #reset
     *
     * @since Servlet 2.3
     */

    public void resetBuffer();
    
    /**
     * レスポンスがすでにコミットされているかどうかを示すbooleanを返します。
     * コミットされたレスポンスはステータスコード及びにヘッダーがすでに書き込まれています。
     *
     * @return  レスポンスがすでにコミットされているかどうかを示すboolean
     *
     * @see #setBufferSize
     * @see #getBufferSize
     * @see #flushBuffer
     * @see #reset
     *
     */
    public boolean isCommitted();
    
    /**
     * Clears any data that exists in the buffer as well as the status code,
     * headers.  The state of calling {@link #getWriter} or
     * {@link #getOutputStream} is also cleared.  It is legal, for instance,
     * to call {@link #getWriter}, {@link #reset} and then
     * {@link #getOutputStream}.  If {@link #getWriter} or
     * {@link #getOutputStream} have been called before this method,
     * then the corrresponding returned Writer or OutputStream will be
     * staled and the behavior of using the stale object is undefined.
     * If the response has been committed, this method throws an 
     * <code>IllegalStateException</code>.
     *
     * @exception IllegalStateException レスポンスがすでにコミットされていた
     *
     * @see #setBufferSize
     * @see #getBufferSize
     * @see #flushBuffer
     * @see #isCommitted
     */
    public void reset();
    
    /**
     * Sets the locale of the response, if the response has not been
     * committed yet. It also sets the response's character encoding
     * appropriately for the locale, if the character encoding has not
     * been explicitly set using {@link #setContentType} or
     * {@link #setCharacterEncoding}, <code>getWriter</code> hasn't
     * been called yet, and the response hasn't been committed yet.
     * If the deployment descriptor contains a 
     * <code>locale-encoding-mapping-list</code> element, and that
     * element provides a mapping for the given locale, that mapping
     * is used. Otherwise, the mapping from locale to character
     * encoding is container dependent.
     * <p>This method may be called repeatedly to change locale and
     * character encoding. The method has no effect if called after the
     * response has been committed. It does not set the response's
     * character encoding if it is called after {@link #setContentType}
     * has been called with a charset specification, after
     * {@link #setCharacterEncoding} has been called, after
     * <code>getWriter</code> has been called, or after the response
     * has been committed.
     * <p>Containers must communicate the locale and the character encoding
     * used for the servlet response's writer to the client if the protocol
     * provides a way for doing so. In the case of HTTP, the locale is
     * communicated via the <code>Content-Language</code> header,
     * the character encoding as part of the <code>Content-Type</code>
     * header for text media types. Note that the character encoding
     * cannot be communicated via HTTP headers if the servlet does not
     * specify a content type; however, it is still used to encode text
     * written via the servlet response's writer.
     * 
     * @param loc レスポンスのロケール
     *
     * @see #getLocale
     * @see #setContentType
     * @see #setCharacterEncoding
     */
    public void setLocale(Locale loc);
    
    /**
     * Returns the locale specified for this response
     * using the {@link #setLocale} method. Calls made to
     * <code>setLocale</code> after the response is committed
     * have no effect. If no locale has been specified,
     * the container's default locale is returned.
     *
     * @return このレスポンスのロケール
     * 
     * @see #setLocale
     */
    public Locale getLocale();

}





