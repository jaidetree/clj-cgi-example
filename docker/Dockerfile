FROM clojure:lein-2.9.1

RUN apt update
RUN apt install --no-install-recommends -yy curl unzip build-essential zlib1g-dev
RUN curl -s https://raw.githubusercontent.com/babashka/babashka/master/install  > ./install \
    && chmod +x ./install \
    && ./install

WORKDIR "/opt"

RUN curl -sLO https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-20.3.0/graalvm-ce-java11-linux-amd64-20.3.0.tar.gz
RUN tar -xzf graalvm-ce-java11-linux-amd64-20.3.0.tar.gz

RUN apt install -yy apache2

RUN a2enmod rewrite
RUN a2enmod cgi

COPY ./conf/apache2.conf /etc/apache2/apache2.conf
COPY ./conf/conf-available/serve-cgi-bin.conf /etc/apache2/conf-available/serve-cgi-bin.conf
COPY ./conf/mods-available/mime.conf /etc/apache2/mods-available/mime.conf
COPY ./conf/sites-available /etc/apache2/sites-available

EXPOSE 80

CMD /usr/sbin/apache2ctl -D FOREGROUND

WORKDIR /var/www/public_html
