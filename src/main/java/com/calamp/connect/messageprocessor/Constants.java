/**
    Dmitri, Arkhipov
    Aug 27, 2015
**/

package com.calamp.connect.messageprocessor;

public final class Constants {
    // Names
    public static final String rootPackageName     = "com.calamp.connect.messageprocessor";
    public static final String stageChannelName    = "calAmpStageChannel";
    public static final String sourceChannelName   = "calAmpSourceChannel";
    public static final String targetChannelName   = "calAmpTargetChannel";
    public static final String errorChannelName    = "calAmpErrorChannel";
    public static final String nextHopHeaderName   = "calamp-next-hop";
    public static final String sqsSetupYamlPrefix  = "sqs_setup";
    public static final String terminalStageTag    = ".";
    public static final String bootOkString        = "Staging Via Spring Boot is Running!";

    // Timings
    public static final long requestTimeoutMillis  = 10000L;
    public static final long sqsPollDelayMillis    = 1000L;
    public static final long replyTimeoutMillis    = 1000L;
    public static final long springPollDelayMillis = 64L;
}
