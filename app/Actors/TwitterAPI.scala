package Actors


trait TwitterAPI {
  val config = new twitter4j.conf.ConfigurationBuilder()
    .setOAuthConsumerKey("RCwSFFcInh40S9s3IZLz5iikU")
    .setOAuthConsumerSecret("nb6BDODfBwrpJGRybdz3owLoFeNnji58U7nVYExFVurTlXhpJd")
    .setOAuthAccessToken("723849736990670849-YPqgZfwpm04ottfSpbJt51vY3fYOCsH")
    .setOAuthAccessTokenSecret("xq483OPxS4YoOj9hId1l8F5sWwNNmR1ONe8xHcVPCRmBo")
    .build
}
