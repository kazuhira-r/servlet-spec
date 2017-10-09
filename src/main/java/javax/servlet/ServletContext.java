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

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.EnumSet;
import java.util.EventListener;
import java.util.Map;
import java.util.Set;
import javax.servlet.descriptor.JspConfigDescriptor;

/**
 * サーブレットがサーブレットコンテナと通信するために使用する一連のメソッドを定義します。
 * 例として、ファイルのMIMEタイプを取得したり、要求をディスパッチしたり、ログファイルに書き込んだりすることができます。
 * 
 * <p>Java仮想マシンごとの"ウェブアプリケーション"ごとに1つのコンテキストがあります。
 * ("ウェブアプリケーション"は、<code>/catalog</code>などのようにサーバーのURL名前空間の特定のサブセットの下に<code>.war</code>ファイル経由でインストールことができるサーブレットとコンテンツの集合です。)
 * 
 * <p>デプロイメントディスクリプタに"distributed"とつけられたウェブアプリケーションの場合は、各仮想マシンごとに1つのコンテキストインスタンスが存在します。
 * この状況では、グローバルな情報を共有するための場所として<code>ServletContext</code>を使用することはできません（その情報は真にグローバルではない方が良いため）。
 * 代わりにデータベースのような外部リソースを使用してください。
 * 
 * <p><code>ServletContext</code>オブジェクトは{@link ServletConfig}オブジェクト内に含まれており、サーブレットが初期化されたときにウェブサーバーがそれらを提供します。
 *
 * @author 	Various
 *
 * @see 	Servlet#getServletConfig
 * @see 	ServletConfig#getServletContext
 */

public interface ServletContext {

    /**
     * <tt>ServletContext</tt>のためにサーブレットコンテナによって提供される(<tt>java.io.File</tt>e型の)プライベートテンポラリディレクトリを格納する<tt>ServletContext</tt>の属性の名です。
     */
    public static final String TEMPDIR = "javax.servlet.context.tempdir";


    /**
     * ウェブフラグメント名で順序付けられた(なし<code>&lt;others/&gt;</code>が使用されていない<code>&lt;absolute-ordering&gt;</code>の場合は除外されることがある)
     * <code>WEB-INF/lib</code>内のJARファイルの名前のリストを含む(<code>java.util.List&lt;java.lang.String&gt;</code>型の)JARファイルの名前のリストを持つ<code>ServletContext</code>の属性の名前です。
     * 絶対順序または相対順序が指定されていない場合はnullです。
     */
    public static final String ORDERED_LIBS =
        "javax.servlet.context.orderedLibs";


    /**
     * このウェブアプリケーションのコンテキストパスを返します。
     *
     * <p>コンテキストパスはリクエストのコンテキストを選択するために使用されるリクエストURIの一部分です。
     * コンテキストパスは、常にリクエストURIの最初に来ます。
     * このコンテキストがWebサーバーのURL名前空間の基底にマッピングされる"ルート"コンテキストの場合このパスは空の文字列になります。
     * それ以外の場合、コンテキストがサーバーの名前空間の基底にマッピングされていない場合、パスは"/"で始まりますが"/"で終わることはありません。
     * 
     * <p>サーブレットコンテナは一つのコンテキストを複数のコンテキストパスにて一致させることができます。
     * このような場合、{@link javax.servlet.http.HttpServletRequest#getContextPath()}はリクエストによって使用される実際のコンテキストパスを返し、
     * このメソッドによって返されるパスとは異なる場合があります。
     * このメソッドによって返されるコンテキストパスはアプリケーションのプライマリもしくは優先コンテキストパスと見なす必要があります。
     *
     * @return ウェブアプリケーションのコンテキスパス、ルートコンテキストの場合は""
     *
     * @see javax.servlet.http.HttpServletRequest#getContextPath()
     *
     * @since Servlet 2.5
     */
    public String getContextPath();


    /**
     * サーバー上の指定されたURLに対応する<code>ServletContext</code>オブジェクトを返します。
     * 
     * <p>このメソッドは、サーブレットがサーバーのさまざまな部分のコンテキストにアクセスできるようにし、
     * 必要に応じてコンテキストから{@link RequestDispatcher}オブジェクトを取得できるようにします。
     * 与えられたパスは<tt>/</tt>で始まる必要があり、
     * サーバーのドキュメントルートからの相対パスとして解釈され、
     * このコンテナでホストされている他のWebアプリケーションのコンテキストルートと照合されます。
     * 
     * <p>セキュリティを意識した環境ではサーブレットコンテナは指定されたURLに対して<code>null</code>を返すことがあります。
     *
     * @param uripath 	このコンテナ内のほかのウェブアプリケーションのコンテキストパスを示す<code>String</code>
     * @return		URLの名前に対応する<code>ServletContext</code>のオブジェクト、存在しないかコンテナがアクセス制限を臨んだ場合はnull
     *
     * @see 		RequestDispatcher
     */
    public ServletContext getContext(String uripath);


    /**
     * このサーブレットコンテナがサポートするServlet APIのメジャーバージョンを返します。
     * バージョン4.0に準拠するすべての実装でこのメソッドは4を返す必要があります。
     *
     * @return 4
     */
    public int getMajorVersion();


    /**
     * このサーブレットコンテナがサポートするServlet APIのマイナーバージョンを返します。
     * バージョン4.0に準拠するすべての実装でこのメソッドは0を返す必要があります。
     *
     * @return 0
     */
    public int getMinorVersion();


    /**
     * このServletContextが表すアプリケーションが基づいているServlet仕様のメジャーバージョンを取得します。
     * 
     * <p>返される値はサーブレットコンテナがサポートするサーブレット仕様のメジャーバージョンを返す{@link #getMajorVersion}とは異なる場合があります。
     *
     * @return このServletContextが表すアプリケーションが基づいているServlet仕様のメジャーバージョン
     *
     * @throws UnsupportedOperationException このServletContextが<code>web.xml</code>や<code>web-fragment.xml</code>で宣言されておらず、
     * {@link javax.servlet.annotation.WebListener}アノテーションもついてない{@link ServletContextListener}の
     * {@link ServletContextListener#contextInitialized}メソッドに渡された場合
     *
     * @since Servlet 3.0
     */
    public int getEffectiveMajorVersion();


    /**
     * このServletContextが表すアプリケーションが基づいているServlet仕様のマイナーバージョンを取得します。
     * 
     * <p>返される値はサーブレットコンテナがサポートするサーブレット仕様のマイナーバージョンを返す{@link #getMinorVersion}とは異なる場合があります。
     *
     * @return このServletContextが表すアプリケーションが基づいているServlet仕様のマイナーバージョン
     *
     * @throws UnsupportedOperationException このServletContextが<code>web.xml</code>や<code>web-fragment.xml</code>で宣言されておらず、
     * {@link javax.servlet.annotation.WebListener}アノテーションもついてない{@link ServletContextListener}の
     * {@link ServletContextListener#contextInitialized}メソッドに渡された場合
     *
     * @since Servlet 3.0
     */
    public int getEffectiveMinorVersion();


    /**
     * 指定されたファイルのMIMEタイプを返します。MIMEタイプが不明な場合はnullを返します。
     * MIMEタイプはサーブレットコンテナの設定とアプリケーションのデプロイメントディスクリプタでの指定で決められます。
     * 一般的なMIMEタイプとしては<code>text/html</code>や<code>image/gif</code>を含みます。
     *
     * @param file ファイルの名前を示す<code>String</code>
     *
     * @return ファイルのMIMEタイプを示す<code>String</code>
     */
    public String getMimeType(String file);


    /**
     * 最も長いサブパスが指定された引数のパスと一致するウェブアプリケーション内のリソースへのディレクトリに似た形式ですべてのパスのリストを返します。

     *
     * <p>サブディレクトリパスを示すパスは<tt>/</tt>で終わります。
     *
     * <p>返されるパスは<tt>/</tt>から始まる、ウェブアプリケーションのルートからの相対パス、
     * もしくはウェブアプリケーションの<tt>/WEB-INF/lib</tt>ディレクトリ内のJARファイルの<tt>/META-INF/resources</tt>ディレクトリからの相対パスです。
     *
     * <p>返されたSetは{@code ServletContext}オブジェクトによって追跡されないため、
     * 返されたSetの変更は{@code ServletContext}オブジェクトに反映されず、その逆もそうです。</p>
     *
     * <p>例として、ウェブアプリケーションが以下のとおりファイルを含むとします。
     *
     * <pre>{@code
     *   /welcome.html
     *   /catalog/index.html
     *   /catalog/products.html
     *   /catalog/offers/books.html
     *   /catalog/offers/music.html
     *   /customer/login.jsp
     *   /WEB-INF/web.xml
     *   /WEB-INF/classes/com.acme.OrderServlet.class
     *   /WEB-INF/lib/catalog.jar!/META-INF/resources/catalog/moreOffers/books.html
     * }</pre>
     *
     * <tt>getResourcePaths("/")</tt>は
     * <tt>{"/welcome.html", "/catalog/", "/customer/", "/WEB-INF/"}</tt>を返し、
     * <tt>getResourcePaths("/catalog/")</tt>は
     * <tt>{"/catalog/index.html", "/catalog/products.html",
     * "/catalog/offers/", "/catalog/moreOffers/"}</tt>を返すでしょう。
     *
     * @param path <tt>/</tt>から始まる必要のあるマッチしたリソースで使用されている部分的なパス
     * 
     * @return ディレクトリ内を一覧化したものを含むSet、ウェうアプリケーション内に指定されたパスで始まるリソースが存在しない場合はnull
     * a Set containing the directory listing, or null if there
     * are no resources in the web application whose path
     * begins with the supplied path.
     *
     * @since Servlet 2.3
     */
    public Set<String> getResourcePaths(String path);


    /**
     * 与えられたパスにマップされたリソースへのURLを返します。
     *
     * <p>パスは<tt>/</tt>で始まり、現在のコンテキストルートからの相対パスとして解決できるか
     * ウェブアプリケーションの<tt>/WEB-INF/lib</tt>ディレクトリ内のJARファイルの<tt>/META-INF/resources</tt>ディレクトリからの相対パス
     * である必要があります。
     * このメソッドはリクエストされたリソースを探すのに、最初にウェブアプリケーションのドキュメントルートを参照します。
     * 次に<tt>/WEB-INF/lib</tt>の中のJARファイルの中身を参照します。
     * <tt>/WEB-INF/lib</tt>の中のJARファイルを探す順番は未定義です。
     * 
     * <p>このメソッドを使用するとサーブレットコンテナは任意のソースからサーブレットでリソースを使用できるようになります。
     * リソースは、ローカルやリモートのファイルシステム、データベース、<code>.war</code>ファイル内に配置できます。
     *
     * <p>サーブレットコンテナはリソースにアクセスするために必要なURLハンドラや<code>URLConnection</code>オブジェクトを実装する必要があります。
     *
     * <p>このメソッドはパスにリソースがマップされていない場合は<code>null</code>を返します。
     *
     * <p>一部のコンテナではURLクラスのメソッドを使用することでこのメ​​ソッドから返されたURLに書き込むことができます。
     *
     * <p>リソースの内容が直接返されるため、<code>.jsp</code>ページを要求するとJSPのソースコードが返されることに注意してください。
     * 実行結果をインクルードするには代わりに<code>RequestDispatcher</code>を使用します。
     *
     * <p>このメソッドはクラスローダーをベースにリソースを探す<code>java.lang.Class.getResource</code>とは目的が違います。
     * このメソッドはクラスローダーを使用しません。
     *
     * @param path リソースのパスを示す<code>String</code>
     *
     * @return パスの位置のリソース、そのパスにリソースが存在しない場合は<code>null</code>
     *
     * @exception MalformedURLException パスが正しい形式でなかった
     */
    public URL getResource(String path) throws MalformedURLException;


    /**
     * パスの所在地のリソースを<code>InputStream</code>オブジェクトとして返します。
     *
     * <p><code>InputStream</code>内のデータはどのようなタイプでも長さでもできます。
     * パスは<code>getResource</code>と同じルールに従って指定される必要があります。
     * このメソッドは指定されたパスにリソースが存在しない場合は<code>null</code>を返します。
     *
     * <p><code>getResource</code>メソッドで使用可能なコンテンツタイプやコンテンツの長さなどのようなメタ情報はこのメソッドを使用すると失われます。
     *
     * <p>サーブレットコンテナはリソースにアクセスするために必要なURLハンドラや<code>URLConnection</code>オブジェクトを実装する必要があります。
     *
     * <p>このメソッドはクラスローダーを使用する<code>java.lang.Class.getResourceAsStream</code>とは違います。
     * このメソッドはサーブレットコンテナにクラスローダーを使用せずに任意の場所からサーブレットでリソースを利用できるようにします。
     *
     *
     * @param path 	リソースのパスを示す<code>String</code>
     *
     * @return 		サーブレットに返す<code>InputStream</code>、指定されたパスにリソースが存在しない場合は<code>null</code>
     */
    public InputStream getResourceAsStream(String path);


    /**
     * 指定されたパスにあるリソースのラッパーとして機能する{@link RequestDispatcher}のオブジェクトを返します。 
     * <code>RequestDispatcher</code>オブジェクトを使用することでリクエストを別のリソースに転送したり、リソースをレスポンスに含めることができます。
     * リソースは動的なものでも静的なものでもかまいません。
     * 
     * <p>指定されたパス名は<tt>/</tt>で始まる必要があり、現在のコンテキストルートからの相対パスとして解釈されます。
     * 
     * <p>このメソッドは<code>ServletContext</code>が<code>RequestDispatcher</code>を返せなかった場合は<code>null</code>を返します。
     *
     * @param path 	リソースのパス名を示す<code>String</code>
     *
     * @return 		指定されたパスにあるリソースのラッパーとして機能する<code>RequestDispatcher</code>のオブジェクト、
     *              <code>ServletContext</code>が<code>RequestDispatcher</code>を返せなかった場合は<code>null</code>
     *
     * @see 		RequestDispatcher
     * @see 		ServletContext#getContext
     */
    public RequestDispatcher getRequestDispatcher(String path);


    /**
     * Returns a {@link RequestDispatcher} object that acts
     * as a wrapper for the named servlet.
     *
     * <p>Servlets (and JSP pages also) may be given names via server
     * administration or via a web application deployment descriptor.
     * A servlet instance can determine its name using
     * {@link ServletConfig#getServletName}.
     *
     * <p>This method returns <code>null</code> if the
     * <code>ServletContext</code>
     * cannot return a <code>RequestDispatcher</code> for any reason.
     *
     * @param name 	a <code>String</code> specifying the name
     *			of a servlet to wrap
     *
     * @return 		a <code>RequestDispatcher</code> object
     *			that acts as a wrapper for the named servlet,
     *			or <code>null</code> if the <code>ServletContext</code>
     *			cannot return a <code>RequestDispatcher</code>
     *
     * @see 		RequestDispatcher
     * @see 		ServletContext#getContext
     * @see 		ServletConfig#getServletName
     */
    public RequestDispatcher getNamedDispatcher(String name);


    /**
     * @deprecated	As of Java Servlet API 2.1, with no direct replacement.
     *
     * <p>This method was originally defined to retrieve a servlet
     * from a <code>ServletContext</code>. In this version, this method
     * always returns <code>null</code> and remains only to preserve
     * binary compatibility. This method will be permanently removed
     * in a future version of the Java Servlet API.
     *
     * <p>In lieu of this method, servlets can share information using the
     * <code>ServletContext</code> class and can perform shared business logic
     * by invoking methods on common non-servlet classes.
     *
     * @param name the servlet name
     * @return the {@code javax.servlet.Servlet Servlet} with the given name
     * @throws ServletException if an exception has occurred that interfaces
     *                          with servlet's normal operation
     */
    @Deprecated
    public Servlet getServlet(String name) throws ServletException;


    /**
     * @deprecated	As of Java Servlet API 2.0, with no replacement.
     *
     * <p>This method was originally defined to return an
     * <code>Enumeration</code> of all the servlets known to this servlet
     * context.
     * In this version, this method always returns an empty enumeration and
     * remains only to preserve binary compatibility. This method
     * will be permanently removed in a future version of the Java
     * Servlet API.
     *
     * @return an <code>Enumeration</code> of {@code javax.servlet.Servlet Servlet}
     */
    @Deprecated
    public Enumeration<Servlet> getServlets();


    /**
     * @deprecated	As of Java Servlet API 2.1, with no replacement.
     *
     * <p>This method was originally defined to return an
     * <code>Enumeration</code>
     * of all the servlet names known to this context. In this version,
     * this method always returns an empty <code>Enumeration</code> and
     * remains only to preserve binary compatibility. This method will
     * be permanently removed in a future version of the Java Servlet API.
     *
     * @return an <code>Enumeration</code> of {@code javax.servlet.Servlet Servlet} names
     */
    @Deprecated
    public Enumeration<String> getServletNames();


    /**
     *
     * Writes the specified message to a servlet log file, usually
     * an event log. The name and type of the servlet log file is
     * specific to the servlet container.
     *
     * @param msg 	a <code>String</code> specifying the
     *			message to be written to the log file
     */
    public void log(String msg);


    /**
     * @deprecated	As of Java Servlet API 2.1, use
     * 			{@link #log(String message, Throwable throwable)}
     *			instead.
     *
     * <p>This method was originally defined to write an
     * exception's stack trace and an explanatory error message
     * to the servlet log file.
     *
     * @param exception the <code>Exception</code> error
     * @param msg a <code>String</code> that describes the exception
     */
    @Deprecated
    public void log(Exception exception, String msg);


    /**
     * Writes an explanatory message and a stack trace
     * for a given <code>Throwable</code> exception
     * to the servlet log file. The name and type of the servlet log
     * file is specific to the servlet container, usually an event log.
     *
     * @param message 		a <code>String</code> that
     *				describes the error or exception
     *
     * @param throwable 	the <code>Throwable</code> error
     *				or exception
     */
    public void log(String message, Throwable throwable);


    /**
     * Gets the <i>real</i> path corresponding to the given
     * <i>virtual</i> path.
     *
     * <p>For example, if <tt>path</tt> is equal to <tt>/index.html</tt>,
     * this method will return the absolute file path on the server's
     * filesystem to which a request of the form
     * <tt>http://&lt;host&gt;:&lt;port&gt;/&lt;contextPath&gt;/index.html</tt>
     * would be mapped, where <tt>&lt;contextPath&gt;</tt> corresponds to the
     * context path of this ServletContext.
     *
     * <p>The real path returned will be in a form
     * appropriate to the computer and operating system on
     * which the servlet container is running, including the
     * proper path separators.
     *
     * <p>Resources inside the <tt>/META-INF/resources</tt>
     * directories of JAR files bundled in the application's
     * <tt>/WEB-INF/lib</tt> directory must be considered only if the
     * container has unpacked them from their containing JAR file, in
     * which case the path to the unpacked location must be returned.
     *
     * <p>This method returns <code>null</code> if the servlet container
     * is unable to translate the given <i>virtual</i> path to a
     * <i>real</i> path.
     *
     * @param path the <i>virtual</i> path to be translated to a
     * <i>real</i> path
     *
     * @return the <i>real</i> path, or <tt>null</tt> if the
     * translation cannot be performed
     */
    public String getRealPath(String path);


    /**
     * Returns the name and version of the servlet container on which
     * the servlet is running.
     *
     * <p>The form of the returned string is
     * <i>servername</i>/<i>versionnumber</i>.
     * For example, the JavaServer Web Development Kit may return the string
     * <code>JavaServer Web Dev Kit/1.0</code>.
     *
     * <p>The servlet container may return other optional information
     * after the primary string in parentheses, for example,
     * <code>JavaServer Web Dev Kit/1.0 (JDK 1.1.6; Windows NT 4.0 x86)</code>.
     *
     *
     * @return 		a <code>String</code> containing at least the
     *			servlet container name and version number
     */
    public String getServerInfo();


    /**
     * Returns a <code>String</code> containing the value of the named
     * context-wide initialization parameter, or <code>null</code> if
     * the parameter does not exist.
     *
     * <p>This method can make available configuration information useful
     * to an entire web application.  For example, it can provide a
     * webmaster's email address or the name of a system that holds
     * critical data.
     *
     * @param	name	a <code>String</code> containing the name of the
     *                  parameter whose value is requested
     *
     * @return a <code>String</code> containing the value of the
     * context's initialization parameter, or <code>null</code> if the
     * context's initialization parameter does not exist.
     *
     * @throws NullPointerException if the argument {@code name} is
     * {@code null}
     *
     * @see ServletConfig#getInitParameter
     */
    public String getInitParameter(String name);


    /**
     * Returns the names of the context's initialization parameters as an
     * <code>Enumeration</code> of <code>String</code> objects, or an
     * empty <code>Enumeration</code> if the context has no initialization
     * parameters.
     *
     * @return 		an <code>Enumeration</code> of <code>String</code>
     *                  objects containing the names of the context's
     *                  initialization parameters
     *
     * @see ServletConfig#getInitParameter
     */
    public Enumeration<String> getInitParameterNames();


    /**
     * Sets the context initialization parameter with the given name and
     * value on this ServletContext.
     *
     * @param name the name of the context initialization parameter to set
     * @param value the value of the context initialization parameter to set
     *
     * @return true if the context initialization parameter with the given
     * name and value was set successfully on this ServletContext, and false
     * if it was not set because this ServletContext already contains a
     * context initialization parameter with a matching name
     *
     * @throws IllegalStateException if this ServletContext has already
     * been initialized
     *
     * @throws NullPointerException if the name parameter is {@code null}
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link ServletContextListener#contextInitialized} method
     * of a {@link ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @since Servlet 3.0
     */
    public boolean setInitParameter(String name, String value);


    /**
     * Returns the servlet container attribute with the given name, or
     * <code>null</code> if there is no attribute by that name.
     *
     * <p>An attribute allows a servlet container to give the
     * servlet additional information not
     * already provided by this interface. See your
     * server documentation for information about its attributes.
     * A list of supported attributes can be retrieved using
     * <code>getAttributeNames</code>.
     *
     * <p>The attribute is returned as a <code>java.lang.Object</code>
     * or some subclass.
     *
     * <p>Attribute names should follow the same convention as package
     * names. The Java Servlet API specification reserves names
     * matching <code>java.*</code>, <code>javax.*</code>,
     * and <code>sun.*</code>.
     *
     * @param name 	a <code>String</code> specifying the name
     *			of the attribute
     *
     * @return an <code>Object</code> containing the value of the
     *			attribute, or <code>null</code> if no attribute
     *			exists matching the given name.
     *
     * @see 		ServletContext#getAttributeNames
     *
     * @throws NullPointerException if the argument {@code name} is
     * {@code null}
     *
     */
    public Object getAttribute(String name);


    /**
     * Returns an <code>Enumeration</code> containing the
     * attribute names available within this ServletContext.
     *
     * <p>Use the {@link #getAttribute} method with an attribute name
     * to get the value of an attribute.
     *
     * @return 		an <code>Enumeration</code> of attribute
     *			names
     *
     * @see		#getAttribute
     */
    public Enumeration<String> getAttributeNames();


    /**
     * Binds an object to a given attribute name in this ServletContext. If
     * the name specified is already used for an attribute, this
     * method will replace the attribute with the new to the new attribute.
     * <p>If listeners are configured on the <code>ServletContext</code> the
     * container notifies them accordingly.
     * <p>
     * If a null value is passed, the effect is the same as calling
     * <code>removeAttribute()</code>.
     *
     * <p>Attribute names should follow the same convention as package
     * names. The Java Servlet API specification reserves names
     * matching <code>java.*</code>, <code>javax.*</code>, and
     * <code>sun.*</code>.
     *
     * @param name 	a <code>String</code> specifying the name
     *			of the attribute
     *
     * @param object 	an <code>Object</code> representing the
     *			attribute to be bound
     *
     * @throws NullPointerException if the name parameter is {@code null}
     *
     */
    public void setAttribute(String name, Object object);


    /**
     * Removes the attribute with the given name from
     * this ServletContext. After removal, subsequent calls to
     * {@link #getAttribute} to retrieve the attribute's value
     * will return <code>null</code>.
     *
     * <p>If listeners are configured on the <code>ServletContext</code> the
     * container notifies them accordingly.
     *
     * @param name	a <code>String</code> specifying the name
     * 			of the attribute to be removed
     */
    public void removeAttribute(String name);


    /**
     * Returns the name of this web application corresponding to this
     * ServletContext as specified in the deployment descriptor for this
     * web application by the display-name element.
     *
     * @return The name of the web application or null if no name has been
     * declared in the deployment descriptor.
     *
     * @since Servlet 2.3
     */
    public String getServletContextName();


    /**
     * Adds the servlet with the given name and class name to this servlet
     * context.
     *
     * <p>The registered servlet may be further configured via the returned
     * {@link ServletRegistration} object.
     *
     * <p>The specified <tt>className</tt> will be loaded using the
     * classloader associated with the application represented by this
     * ServletContext.
     *
     * <p>If this ServletContext already contains a preliminary
     * ServletRegistration for a servlet with the given <tt>servletName</tt>,
     * it will be completed (by assigning the given <tt>className</tt> to it)
     * and returned.
     *
     * <p>This method introspects the class with the given <tt>className</tt>
     * for the {@link javax.servlet.annotation.ServletSecurity},
     * {@link javax.servlet.annotation.MultipartConfig},
     * <tt>javax.annotation.security.RunAs</tt>, and
     * <tt>javax.annotation.security.DeclareRoles</tt> annotations.
     * In addition, this method supports resource injection if the
     * class with the given <tt>className</tt> represents a Managed Bean.
     * See the Java EE platform and JSR 299 specifications for additional
     * details about Managed Beans and resource injection.
     *
     * @param servletName the name of the servlet
     * @param className the fully qualified class name of the servlet
     *
     * @return a ServletRegistration object that may be used to further
     * configure the registered servlet, or <tt>null</tt> if this
     * ServletContext already contains a complete ServletRegistration for
     * a servlet with the given <tt>servletName</tt>
     *
     * @throws IllegalStateException if this ServletContext has already
     * been initialized
     *
     * @throws IllegalArgumentException if <code>servletName</code> is null
     * or an empty String
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link ServletContextListener#contextInitialized} method
     * of a {@link ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @since Servlet 3.0
     */
    public ServletRegistration.Dynamic addServlet(
        String servletName, String className);


    /**
     * Registers the given servlet instance with this ServletContext
     * under the given <tt>servletName</tt>.
     *
     * <p>The registered servlet may be further configured via the returned
     * {@link ServletRegistration} object.
     *
     * <p>If this ServletContext already contains a preliminary
     * ServletRegistration for a servlet with the given <tt>servletName</tt>,
     * it will be completed (by assigning the class name of the given servlet
     * instance to it) and returned.
     *
     * @param servletName the name of the servlet
     * @param servlet the servlet instance to register
     *
     * @return a ServletRegistration object that may be used to further
     * configure the given servlet, or <tt>null</tt> if this
     * ServletContext already contains a complete ServletRegistration for a
     * servlet with the given <tt>servletName</tt> or if the same servlet
     * instance has already been registered with this or another
     * ServletContext in the same container
     *
     * @throws IllegalStateException if this ServletContext has already
     * been initialized
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link ServletContextListener#contextInitialized} method
     * of a {@link ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @throws IllegalArgumentException if the given servlet instance
     * implements {@link SingleThreadModel}, or <code>servletName</code> is null
     * or an empty String
     *
     * @since Servlet 3.0
     */
    public ServletRegistration.Dynamic addServlet(
        String servletName, Servlet servlet);


    /**
     * Adds the servlet with the given name and class type to this servlet
     * context.
     *
     * <p>The registered servlet may be further configured via the returned
     * {@link ServletRegistration} object.
     *
     * <p>If this ServletContext already contains a preliminary
     * ServletRegistration for a servlet with the given <tt>servletName</tt>,
     * it will be completed (by assigning the name of the given
     * <tt>servletClass</tt> to it) and returned.
     *
     * <p>This method introspects the given <tt>servletClass</tt> for
     * the {@link javax.servlet.annotation.ServletSecurity},
     * {@link javax.servlet.annotation.MultipartConfig},
     * <tt>javax.annotation.security.RunAs</tt>, and
     * <tt>javax.annotation.security.DeclareRoles</tt> annotations.
     * In addition, this method supports resource injection if the
     * given <tt>servletClass</tt> represents a Managed Bean.
     * See the Java EE platform and JSR 299 specifications for additional
     * details about Managed Beans and resource injection.
     *
     * @param servletName the name of the servlet
     * @param servletClass the class object from which the servlet will be
     * instantiated
     *
     * @return a ServletRegistration object that may be used to further
     * configure the registered servlet, or <tt>null</tt> if this
     * ServletContext already contains a complete ServletRegistration for
     * the given <tt>servletName</tt>
     *
     * @throws IllegalStateException if this ServletContext has already
     * been initialized
     *
     * @throws IllegalArgumentException if <code>servletName</code> is null
     * or an empty String
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link ServletContextListener#contextInitialized} method
     * of a {@link ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @since Servlet 3.0
     */
    public ServletRegistration.Dynamic addServlet(String servletName,
        Class <? extends Servlet> servletClass);


    /**
     * Adds the servlet with the given jsp file to this servlet context.
     *
     * <p>The registered servlet may be further configured via the returned
     * {@link ServletRegistration} object.
     *
     * <p>If this ServletContext already contains a preliminary
     * ServletRegistration for a servlet with the given <tt>servletName</tt>,
     * it will be completed (by assigning the given <tt>jspFile</tt> to it)
     * and returned.
     *
     * @param servletName the name of the servlet
     * @param jspFile the full path to a JSP file within the web application
     *                beginning with a `/'.
     *
     * @return a ServletRegistration object that may be used to further
     * configure the registered servlet, or <tt>null</tt> if this
     * ServletContext already contains a complete ServletRegistration for
     * a servlet with the given <tt>servletName</tt>
     *
     * @throws IllegalStateException if this ServletContext has already
     * been initialized
     *
     * @throws IllegalArgumentException if <code>servletName</code> is null
     * or an empty String
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link ServletContextListener#contextInitialized} method
     * of a {@link ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @since Servlet 4.0
     */
    public ServletRegistration.Dynamic addJspFile(
        String servletName, String jspFile);


    /**
     * Instantiates the given Servlet class.
     *
     * <p>The returned Servlet instance may be further customized before it
     * is registered with this ServletContext via a call to
     * {@link #addServlet(String,Servlet)}.
     *
     * <p>The given Servlet class must define a zero argument constructor,
     * which is used to instantiate it.
     *
     * <p>This method introspects the given <tt>clazz</tt> for
     * the following annotations:
     * {@link javax.servlet.annotation.ServletSecurity},
     * {@link javax.servlet.annotation.MultipartConfig},
     * <tt>javax.annotation.security.RunAs</tt>, and
     * <tt>javax.annotation.security.DeclareRoles</tt>.
     * In addition, this method supports resource injection if the
     * given <tt>clazz</tt> represents a Managed Bean.
     * See the Java EE platform and JSR 299 specifications for additional
     * details about Managed Beans and resource injection.
     *
     * @param <T> the class of the Servlet to create
     * @param clazz the Servlet class to instantiate
     *
     * @return the new Servlet instance
     *
     * @throws ServletException if the given <tt>clazz</tt> fails to be
     * instantiated
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link ServletContextListener#contextInitialized} method
     * of a {@link ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @since Servlet 3.0
     */
    public <T extends Servlet> T createServlet(Class<T> clazz)
        throws ServletException;


    /**
     * Gets the ServletRegistration corresponding to the servlet with the
     * given <tt>servletName</tt>.
     *
     * @return the (complete or preliminary) ServletRegistration for the
     * servlet with the given <tt>servletName</tt>, or null if no
     * ServletRegistration exists under that name
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link ServletContextListener#contextInitialized} method
     * of a {@link ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @param servletName the name of a servlet
     * @since Servlet 3.0
     */
    public ServletRegistration getServletRegistration(String servletName);


    /**
     * Gets a (possibly empty) Map of the ServletRegistration
     * objects (keyed by servlet name) corresponding to all servlets
     * registered with this ServletContext.
     *
     * <p>The returned Map includes the ServletRegistration objects
     * corresponding to all declared and annotated servlets, as well as the
     * ServletRegistration objects corresponding to all servlets that have
     * been added via one of the <tt>addServlet</tt> and <tt>addJspFile</tt>
     * methods.
     *
     * <p>If permitted, any changes to the returned Map must not affect this
     * ServletContext.
     *
     * @return Map of the (complete and preliminary) ServletRegistration
     * objects corresponding to all servlets currently registered with this
     * ServletContext
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link ServletContextListener#contextInitialized} method
     * of a {@link ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @since Servlet 3.0
     */
    public Map<String, ? extends ServletRegistration> getServletRegistrations();


    /**
     * Adds the filter with the given name and class name to this servlet
     * context.
     *
     * <p>The registered filter may be further configured via the returned
     * {@link FilterRegistration} object.
     *
     * <p>The specified <tt>className</tt> will be loaded using the
     * classloader associated with the application represented by this
     * ServletContext.
     *
     * <p>If this ServletContext already contains a preliminary
     * FilterRegistration for a filter with the given <tt>filterName</tt>,
     * it will be completed (by assigning the given <tt>className</tt> to it)
     * and returned.
     *
     * <p>This method supports resource injection if the class with the
     * given <tt>className</tt> represents a Managed Bean.
     * See the Java EE platform and JSR 299 specifications for additional
     * details about Managed Beans and resource injection.
     *
     * @param filterName the name of the filter
     * @param className the fully qualified class name of the filter
     *
     * @return a FilterRegistration object that may be used to further
     * configure the registered filter, or <tt>null</tt> if this
     * ServletContext already contains a complete FilterRegistration for
     * a filter with the given <tt>filterName</tt>
     *
     * @throws IllegalStateException if this ServletContext has already
     * been initialized
     *
     * @throws IllegalArgumentException if <code>filterName</code> is null or
     * an empty String
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link ServletContextListener#contextInitialized} method
     * of a {@link ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @since Servlet 3.0
     */
    public FilterRegistration.Dynamic addFilter(
        String filterName, String className);


    /**
     * Registers the given filter instance with this ServletContext
     * under the given <tt>filterName</tt>.
     *
     * <p>The registered filter may be further configured via the returned
     * {@link FilterRegistration} object.
     *
     * <p>If this ServletContext already contains a preliminary
     * FilterRegistration for a filter with the given <tt>filterName</tt>,
     * it will be completed (by assigning the class name of the given filter
     * instance to it) and returned.
     *
     * @param filterName the name of the filter
     * @param filter the filter instance to register
     *
     * @return a FilterRegistration object that may be used to further
     * configure the given filter, or <tt>null</tt> if this
     * ServletContext already contains a complete FilterRegistration for a
     * filter with the given <tt>filterName</tt> or if the same filter
     * instance has already been registered with this or another
     * ServletContext in the same container
     *
     * @throws IllegalStateException if this ServletContext has already
     * been initialized
     *
     * @throws IllegalArgumentException if <code>filterName</code> is null or
     * an empty String
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link ServletContextListener#contextInitialized} method
     * of a {@link ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @since Servlet 3.0
     */
    public FilterRegistration.Dynamic addFilter(
        String filterName, Filter filter);


    /**
     * Adds the filter with the given name and class type to this servlet
     * context.
     *
     * <p>The registered filter may be further configured via the returned
     * {@link FilterRegistration} object.
     *
     * <p>If this ServletContext already contains a preliminary
     * FilterRegistration for a filter with the given <tt>filterName</tt>,
     * it will be completed (by assigning the name of the given
     * <tt>filterClass</tt> to it) and returned.
     *
     * <p>This method supports resource injection if the given
     * <tt>filterClass</tt> represents a Managed Bean.
     * See the Java EE platform and JSR 299 specifications for additional
     * details about Managed Beans and resource injection.
     *
     * @param filterName the name of the filter
     * @param filterClass the class object from which the filter will be
     * instantiated
     *
     * @return a FilterRegistration object that may be used to further
     * configure the registered filter, or <tt>null</tt> if this
     * ServletContext already contains a complete FilterRegistration for a
     * filter with the given <tt>filterName</tt>
     *
     * @throws IllegalStateException if this ServletContext has already
     * been initialized
     *
     * @throws IllegalArgumentException if <code>filterName</code> is null or
     * an empty String
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link ServletContextListener#contextInitialized} method
     * of a {@link ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @since Servlet 3.0
     */
    public FilterRegistration.Dynamic addFilter(String filterName,
        Class <? extends Filter> filterClass);


    /**
     * Instantiates the given Filter class.
     *
     * <p>The returned Filter instance may be further customized before it
     * is registered with this ServletContext via a call to
     * {@link #addFilter(String,Filter)}.
     *
     * <p>The given Filter class must define a zero argument constructor,
     * which is used to instantiate it.
     *
     * <p>This method supports resource injection if the given
     * <tt>clazz</tt> represents a Managed Bean.
     * See the Java EE platform and JSR 299 specifications for additional
     * details about Managed Beans and resource injection.
     *
     * @param <T> the class of the Filter to create
     * @param clazz the Filter class to instantiate
     *
     * @return the new Filter instance
     *
     * @throws ServletException if the given <tt>clazz</tt> fails to be
     * instantiated
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link ServletContextListener#contextInitialized} method
     * of a {@link ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @since Servlet 3.0
     */
    public <T extends Filter> T createFilter(Class<T> clazz)
        throws ServletException;


    /**
     * Gets the FilterRegistration corresponding to the filter with the
     * given <tt>filterName</tt>.
     *
     * @param filterName the name of a filter
     * @return the (complete or preliminary) FilterRegistration for the
     * filter with the given <tt>filterName</tt>, or null if no
     * FilterRegistration exists under that name
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link ServletContextListener#contextInitialized} method
     * of a {@link ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @since Servlet 3.0
     */
    public FilterRegistration getFilterRegistration(String filterName);


    /**
     * Gets a (possibly empty) Map of the FilterRegistration
     * objects (keyed by filter name) corresponding to all filters
     * registered with this ServletContext.
     *
     * <p>The returned Map includes the FilterRegistration objects
     * corresponding to all declared and annotated filters, as well as the
     * FilterRegistration objects corresponding to all filters that have
     * been added via one of the <tt>addFilter</tt> methods.
     *
     * <p>Any changes to the returned Map must not affect this
     * ServletContext.
     *
     * @return Map of the (complete and preliminary) FilterRegistration
     * objects corresponding to all filters currently registered with this
     * ServletContext
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link ServletContextListener#contextInitialized} method
     * of a {@link ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @since Servlet 3.0
     */
    public Map<String, ? extends FilterRegistration> getFilterRegistrations();


    /**
     * Gets the {@link SessionCookieConfig} object through which various
     * properties of the session tracking cookies created on behalf of this
     * <tt>ServletContext</tt> may be configured.
     *
     * <p>Repeated invocations of this method will return the same
     * <tt>SessionCookieConfig</tt> instance.
     *
     * @return the <tt>SessionCookieConfig</tt> object through which
     * various properties of the session tracking cookies created on
     * behalf of this <tt>ServletContext</tt> may be configured
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link ServletContextListener#contextInitialized} method
     * of a {@link ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @since Servlet 3.0
     */
    public SessionCookieConfig getSessionCookieConfig();


    /**
     * Sets the session tracking modes that are to become effective for this
     * <tt>ServletContext</tt>.
     *
     * <p>The given <tt>sessionTrackingModes</tt> replaces any
     * session tracking modes set by a previous invocation of this
     * method on this <tt>ServletContext</tt>.
     *
     * @param sessionTrackingModes the set of session tracking modes to
     * become effective for this <tt>ServletContext</tt>
     *
     * @throws IllegalStateException if this ServletContext has already
     * been initialized
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link ServletContextListener#contextInitialized} method
     * of a {@link ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @throws IllegalArgumentException if <tt>sessionTrackingModes</tt>
     * specifies a combination of <tt>SessionTrackingMode.SSL</tt> with a
     * session tracking mode other than <tt>SessionTrackingMode.SSL</tt>,
     * or if <tt>sessionTrackingModes</tt> specifies a session tracking mode
     * that is not supported by the servlet container
     *
     * @since Servlet 3.0
     */
    public void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes);


    /**
     * Gets the session tracking modes that are supported by default for this
     * <tt>ServletContext</tt>.
     *
     * <p>The returned set is not backed by the {@code ServletContext} object,
     * so changes in the returned set are not reflected in the
     * {@code ServletContext} object, and vice-versa.</p>
     *
     * @return set of the session tracking modes supported by default for
     * this <tt>ServletContext</tt>
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link ServletContextListener#contextInitialized} method
     * of a {@link ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @since Servlet 3.0
     */
    public Set<SessionTrackingMode> getDefaultSessionTrackingModes();


    /**
     * Gets the session tracking modes that are in effect for this
     * <tt>ServletContext</tt>.
     *
     * <p>The session tracking modes in effect are those provided to
     * {@link #setSessionTrackingModes setSessionTrackingModes}.
     *
     * <p>The returned set is not backed by the {@code ServletContext} object,
     * so changes in the returned set are not reflected in the
     * {@code ServletContext} object, and vice-versa.</p>
     *
     * @return set of the session tracking modes in effect for this
     * <tt>ServletContext</tt>
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link ServletContextListener#contextInitialized} method
     * of a {@link ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @since Servlet 3.0
     */
    public Set<SessionTrackingMode> getEffectiveSessionTrackingModes();


    /**
     * Adds the listener with the given class name to this ServletContext.
     *
     * <p>The class with the given name will be loaded using the
     * classloader associated with the application represented by this
     * ServletContext, and must implement one or more of the following
     * interfaces:
     * <ul>
     * <li>{@link ServletContextAttributeListener}
     * <li>{@link ServletRequestListener}
     * <li>{@link ServletRequestAttributeListener}
     * <li>{@link javax.servlet.http.HttpSessionAttributeListener}
     * <li>{@link javax.servlet.http.HttpSessionIdListener}
     * <li>{@link javax.servlet.http.HttpSessionListener}
     * </ul>
     *
     * <p>If this ServletContext was passed to
     * {@link ServletContainerInitializer#onStartup}, then the class with
     * the given name may also implement {@link ServletContextListener},
     * in addition to the interfaces listed above.
     *
     * <p>As part of this method call, the container must load the class
     * with the specified class name to ensure that it implements one of
     * the required interfaces.
     *
     * <p>If the class with the given name implements a listener interface
     * whose invocation order corresponds to the declaration order (i.e.,
     * if it implements {@link ServletRequestListener},
     * {@link ServletContextListener}, or
     * {@link javax.servlet.http.HttpSessionListener}),
     * then the new listener will be added to the end of the ordered list of
     * listeners of that interface.
     *
     * <p>This method supports resource injection if the class with the
     * given <tt>className</tt> represents a Managed Bean.
     * See the Java EE platform and JSR 299 specifications for additional
     * details about Managed Beans and resource injection.
     *
     * @param className the fully qualified class name of the listener
     *
     * @throws IllegalArgumentException if the class with the given name
     * does not implement any of the above interfaces, or if it implements
     * {@link ServletContextListener} and this ServletContext was not
     * passed to {@link ServletContainerInitializer#onStartup}
     *
     * @throws IllegalStateException if this ServletContext has already
     * been initialized
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link ServletContextListener#contextInitialized} method
     * of a {@link ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @since Servlet 3.0
     */
    public void addListener(String className);


    /**
     * Adds the given listener to this ServletContext.
     *
     * <p>The given listener must be an instance of one or more of the
     * following interfaces:
     * <ul>
     * <li>{@link ServletContextAttributeListener}
     * <li>{@link ServletRequestListener}
     * <li>{@link ServletRequestAttributeListener}
     * <li>{@link javax.servlet.http.HttpSessionAttributeListener}
     * <li>{@link javax.servlet.http.HttpSessionIdListener}
     * <li>{@link javax.servlet.http.HttpSessionListener}
     * </ul>
     *
     * <p>If this ServletContext was passed to
     * {@link ServletContainerInitializer#onStartup}, then the given
     * listener may also be an instance of {@link ServletContextListener},
     * in addition to the interfaces listed above.
     *
     * <p>If the given listener is an instance of a listener interface whose
     * invocation order corresponds to the declaration order (i.e., if it
     * is an instance of {@link ServletRequestListener},
     * {@link ServletContextListener}, or
     * {@link javax.servlet.http.HttpSessionListener}),
     * then the listener will be added to the end of the ordered list of
     * listeners of that interface.
     *
     * @param <T> the class of the EventListener to add
     * @param t the listener to be added
     *
     * @throws IllegalArgumentException if the given listener is not
     * an instance of any of the above interfaces, or if it is an instance
     * of {@link ServletContextListener} and this ServletContext was not
     * passed to {@link ServletContainerInitializer#onStartup}
     *
     * @throws IllegalStateException if this ServletContext has already
     * been initialized
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link ServletContextListener#contextInitialized} method
     * of a {@link ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @since Servlet 3.0
     */
    public <T extends EventListener> void addListener(T t);


    /**
     * Adds a listener of the given class type to this ServletContext.
     *
     * <p>The given <tt>listenerClass</tt> must implement one or more of the
     * following interfaces:
     * <ul>
     * <li>{@link ServletContextAttributeListener}
     * <li>{@link ServletRequestListener}
     * <li>{@link ServletRequestAttributeListener}
     * <li>{@link javax.servlet.http.HttpSessionAttributeListener}
     * <li>{@link javax.servlet.http.HttpSessionIdListener}
     * <li>{@link javax.servlet.http.HttpSessionListener}
     * </ul>
     *
     * <p>If this ServletContext was passed to
     * {@link ServletContainerInitializer#onStartup}, then the given
     * <tt>listenerClass</tt> may also implement
     * {@link ServletContextListener}, in addition to the interfaces listed
     * above.
     *
     * <p>If the given <tt>listenerClass</tt> implements a listener
     * interface whose invocation order corresponds to the declaration order
     * (i.e., if it implements {@link ServletRequestListener},
     * {@link ServletContextListener}, or
     * {@link javax.servlet.http.HttpSessionListener}),
     * then the new listener will be added to the end of the ordered list
     * of listeners of that interface.
     *
     * <p>This method supports resource injection if the given
     * <tt>listenerClass</tt> represents a Managed Bean.
     * See the Java EE platform and JSR 299 specifications for additional
     * details about Managed Beans and resource injection.
     *
     * @param listenerClass the listener class to be instantiated
     *
     * @throws IllegalArgumentException if the given <tt>listenerClass</tt>
     * does not implement any of the above interfaces, or if it implements
     * {@link ServletContextListener} and this ServletContext was not passed
     * to {@link ServletContainerInitializer#onStartup}
     *
     * @throws IllegalStateException if this ServletContext has already
     * been initialized
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link ServletContextListener#contextInitialized} method
     * of a {@link ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @since Servlet 3.0
     */
    public void addListener(Class <? extends EventListener> listenerClass);


    /**
     * Instantiates the given EventListener class.
     *
     * <p>The specified EventListener class must implement at least one of
     * the {@link ServletContextListener},
     * {@link ServletContextAttributeListener},
     * {@link ServletRequestListener},
     * {@link ServletRequestAttributeListener},
     * {@link javax.servlet.http.HttpSessionAttributeListener},
     * {@link javax.servlet.http.HttpSessionIdListener}, or
     * {@link javax.servlet.http.HttpSessionListener}
     * interfaces.
     *
     * <p>The returned EventListener instance may be further customized
     * before it is registered with this ServletContext via a call to
     * {@link #addListener(EventListener)}.
     *
     * <p>The given EventListener class must define a zero argument
     * constructor, which is used to instantiate it.
     *
     * <p>This method supports resource injection if the given
     * <tt>clazz</tt> represents a Managed Bean.
     * See the Java EE platform and JSR 299 specifications for additional
     * details about Managed Beans and resource injection.
     *
     * @param <T> the class of the EventListener to create
     * @param clazz the EventListener class to instantiate
     *
     * @return the new EventListener instance
     *
     * @throws ServletException if the given <tt>clazz</tt> fails to be
     * instantiated
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link ServletContextListener#contextInitialized} method
     * of a {@link ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @throws IllegalArgumentException if the specified EventListener class
     * does not implement any of the
     * {@link ServletContextListener},
     * {@link ServletContextAttributeListener},
     * {@link ServletRequestListener},
     * {@link ServletRequestAttributeListener},
     * {@link javax.servlet.http.HttpSessionAttributeListener},
     * {@link javax.servlet.http.HttpSessionIdListener}, or
     * {@link javax.servlet.http.HttpSessionListener}
     * interfaces.
     *
     * @since Servlet 3.0
     */
    public <T extends EventListener> T createListener(Class<T> clazz)
        throws ServletException;


    /**
     * Gets the <code>&lt;jsp-config&gt;</code> related configuration
     * that was aggregated from the <code>web.xml</code> and
     * <code>web-fragment.xml</code> descriptor files of the web application
     * represented by this ServletContext.
     *
     * @return the <code>&lt;jsp-config&gt;</code> related configuration
     * that was aggregated from the <code>web.xml</code> and
     * <code>web-fragment.xml</code> descriptor files of the web application
     * represented by this ServletContext, or null if no such configuration
     * exists
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link ServletContextListener#contextInitialized} method
     * of a {@link ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @see javax.servlet.descriptor.JspConfigDescriptor
     *
     * @since Servlet 3.0
     */
    public JspConfigDescriptor getJspConfigDescriptor();


    /**
     * Gets the class loader of the web application represented by this
     * ServletContext.
     *
     * <p>If a security manager exists, and the caller's class loader
     * is not the same as, or an ancestor of the requested class loader,
     * then the security manager's <code>checkPermission</code> method is
     * called with a <code>RuntimePermission("getClassLoader")</code>
     * permission to check whether access to the requested class loader
     * should be granted.
     *
     * @return the class loader of the web application represented by this
     * ServletContext
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link ServletContextListener#contextInitialized} method
     * of a {@link ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @throws SecurityException if a security manager denies access to
     * the requested class loader
     *
     * @since Servlet 3.0
     */
    public ClassLoader getClassLoader();


    /**
     * Declares role names that are tested using <code>isUserInRole</code>.
     *
     * <p>Roles that are implicitly declared as a result of their use within
     * the {@link ServletRegistration.Dynamic#setServletSecurity
     * setServletSecurity} or {@link ServletRegistration.Dynamic#setRunAsRole
     * setRunAsRole} methods of the {@link ServletRegistration} interface need
     * not be declared.
     *
     * @param roleNames the role names being declared
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link ServletContextListener#contextInitialized} method
     * of a {@link ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @throws IllegalArgumentException if any of the argument roleNames is
     * null or the empty string
     *
     * @throws IllegalStateException if the ServletContext has already
     * been initialized
     *
     * @since Servlet 3.0
     */
    public void declareRoles(String... roleNames);


    /**
     * Returns the configuration name of the logical host on which the
     * ServletContext is deployed.
     *
     * Servlet containers may support multiple logical hosts. This method must
     * return the same name for all the servlet contexts deployed on a logical
     * host, and the name returned by this method must be distinct, stable per
     * logical host, and suitable for use in associating server configuration
     * information with the logical host. The returned value is NOT expected
     * or required to be equivalent to a network address or hostname of the
     * logical host.
     *
     * @return a <code>String</code> containing the configuration name of the
     * logical host on which the servlet context is deployed.
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link ServletContextListener#contextInitialized} method
     * of a {@link ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @since Servlet 3.1
     */
    public String getVirtualServerName();


    /**
     * Gets the session timeout in minutes that are supported by default for
     * this <tt>ServletContext</tt>.
     *
     * @return the session timeout in minutes that are supported by default for
     * this <tt>ServletContext</tt>
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link ServletContextListener#contextInitialized} method
     * of a {@link ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @since Servlet 4.0
     */
    public int getSessionTimeout();


    /**
     * Sets the session timeout in minutes for this ServletContext.
     *
     * @param sessionTimeout session timeout in minutes
     *
     * @throws IllegalStateException if this ServletContext has already
     * been initialized
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link ServletContextListener#contextInitialized} method
     * of a {@link ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @since Servlet 4.0
     */
    public void setSessionTimeout(int sessionTimeout);


    /**
     * Gets the request character encoding that are supported by default for
     * this <tt>ServletContext</tt>. This method returns null if no request
     * encoding character encoding has been specified in deployment descriptor
     * or container specific configuration (for all web applications in the
     * container).
     *
     * @return the request character encoding that are supported by default for
     * this <tt>ServletContext</tt>
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link ServletContextListener#contextInitialized} method
     * of a {@link ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @since Servlet 4.0
     */
    public String getRequestCharacterEncoding();


    /**
     * Sets the request character encoding for this ServletContext.
     *
     * @param encoding request character encoding
     *
     * @throws IllegalStateException if this ServletContext has already
     * been initialized
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link ServletContextListener#contextInitialized} method
     * of a {@link ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @since Servlet 4.0
     */
    public void setRequestCharacterEncoding(String encoding);


    /**
     * Gets the response character encoding that are supported by default for
     * this <tt>ServletContext</tt>. This method returns null if no response
     * encoding character encoding has been specified in deployment descriptor
     * or container specific configuration (for all web applications in the
     * container).
     *
     * @return the request character encoding that are supported by default for
     * this <tt>ServletContext</tt>
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link ServletContextListener#contextInitialized} method
     * of a {@link ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @since Servlet 4.0
     */
    public String getResponseCharacterEncoding();


    /**
     * Sets the response character encoding for this ServletContext.
     *
     * @param encoding response character encoding
     *
     * @throws IllegalStateException if this ServletContext has already
     * been initialized
     *
     * @throws UnsupportedOperationException if this ServletContext was
     * passed to the {@link ServletContextListener#contextInitialized} method
     * of a {@link ServletContextListener} that was neither declared in
     * <code>web.xml</code> or <code>web-fragment.xml</code>, nor annotated
     * with {@link javax.servlet.annotation.WebListener}
     *
     * @since Servlet 4.0
     */
    public void setResponseCharacterEncoding(String encoding);
}
