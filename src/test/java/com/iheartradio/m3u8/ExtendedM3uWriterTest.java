package com.iheartradio.m3u8;

import com.iheartradio.m3u8.data.*;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ExtendedM3uWriterTest {

    @Test
    public void testExtXProgramDateTimeChunkPlaylistWriting() throws Exception {
        // given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PlaylistWriter extendedM3uWriter = new PlaylistWriter(outputStream, Format.EXT_M3U, Encoding.UTF_8);

        List<TrackData> trackData = new ArrayList<>();
        trackData.add(new TrackData.Builder().withUri("uri/valami1.m4v").withMapInfo(new MapInfo("uri/valami1.m4vh", null)).withTrackInfo(new TrackInfo(6, "title1"))
                .withProgramDateTime("1970-01-18T21:43:59.264+00:00")
                .build());
        trackData.add(new TrackData.Builder().withUri("uri/valami2.m4v").withMapInfo(new MapInfo("uri/valami1.m4vh", null)).withTrackInfo(new TrackInfo(6, "title2"))
                .withProgramDateTime("1970-01-18T21:43:59.264+00:00")
                .build());
        trackData.add(new TrackData.Builder().withUri("uri/valami3.m4v").withMapInfo(new MapInfo("uri/valami1.m4vh", null)).withTrackInfo(new TrackInfo(6, "title3"))
                .withProgramDateTime("1970-01-18T21:43:59.264+00:00")
                .build());

        MediaPlaylist mediaPlaylist = new MediaPlaylist.Builder().withTracks(trackData).build();
        Playlist playlist = new Playlist.Builder().withMediaPlaylist(mediaPlaylist).build();

        // when
        extendedM3uWriter.write(playlist);

        // then
        String generatedPlaylist = outputStream.toString(Encoding.UTF_8.value);

        String expectedPlaylist = "#EXTM3U\n" +
                "#EXT-X-VERSION:1\n" +
                "#EXT-X-TARGETDURATION:0\n" +
                "#EXT-X-MEDIA-SEQUENCE:0\n" +
                "#EXT-X-ALLOW-CACHE:NO\n" +
                "#EXT-X-MAP:URI=\"uri/valami1.m4vh\"\n" +
                "#EXT-X-PROGRAM-DATE-TIME:1970-01-18T21:43:59.264+00:00\n" +
                "#EXTINF:6,title1\n" +
                "uri/valami1.m4v\n" +
                "#EXT-X-PROGRAM-DATE-TIME:1970-01-18T21:43:59.264+00:00\n" +
                "#EXTINF:6,title2\n" +
                "uri/valami2.m4v\n" +
                "#EXT-X-PROGRAM-DATE-TIME:1970-01-18T21:43:59.264+00:00\n" +
                "#EXTINF:6,title3\n" +
                "uri/valami3.m4v\n" +
                "#EXT-X-ENDLIST\n";

        assertThat("Invalid generated playlist", generatedPlaylist, is(expectedPlaylist));
    }

    @Test
    public void testMapUriChunkPlaylistWriting() throws Exception {
        // given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PlaylistWriter extendedM3uWriter = new PlaylistWriter(outputStream, Format.EXT_M3U, Encoding.UTF_8);

        List<TrackData> trackData = new ArrayList<>();
        trackData.add(new TrackData.Builder().withUri("uri/valami1.m4v").withTrackInfo(new TrackInfo(6, "title1")).build());
        trackData.add(new TrackData.Builder().withUri("uri/valami2.m4v").withTrackInfo(new TrackInfo(6, "title2")).build());
        trackData.add(new TrackData.Builder().withUri("uri/valami3.m4v").withTrackInfo(new TrackInfo(6, "title3")).build());

        MediaPlaylist mediaPlaylist = new MediaPlaylist.Builder().withTracks(trackData).build();
        Playlist playlist = new Playlist.Builder().withMediaPlaylist(mediaPlaylist).build();

        // when
        extendedM3uWriter.write(playlist);

        // then
        String generatedPlaylist = outputStream.toString(Encoding.UTF_8.value);

        String expectedPlaylist = "#EXTM3U\n" +
                "#EXT-X-VERSION:1\n" +
                "#EXT-X-TARGETDURATION:0\n" +
                "#EXT-X-MEDIA-SEQUENCE:0\n" +
                "#EXT-X-ALLOW-CACHE:NO\n" +
                "#EXTINF:6,title1\n" +
                "uri/valami1.m4v\n" +
                "#EXTINF:6,title2\n" +
                "uri/valami2.m4v\n" +
                "#EXTINF:6,title3\n" +
                "uri/valami3.m4v\n" +
                "#EXT-X-ENDLIST\n";

        assertThat("Invalid generated playlist", generatedPlaylist, is(expectedPlaylist));
    }

    @Test
    public void testIndependentSegmentsMasterPlaylistWriting() throws Exception {
        // given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PlaylistWriter extendedM3uWriter = new PlaylistWriter(outputStream, Format.EXT_M3U, Encoding.UTF_8);

        String url1 = "https://host/playlist/auditorium/channel/1524/4/playlist.m3u8";
        PlaylistData playlistData1 = new PlaylistData.Builder()
                .withPath(url1)
                .withStreamInfo(new StreamInfo.Builder().withCodecs(Arrays.asList("mp4a.40.2,avc1.77.31")).withBandwidth(356352).withResolution(new Resolution(864, 486)).withClosedCaptions("NONE").withAudio("audiogroup").build()).withUri(url1)
                .build();

        String url2 = "https://host/playlist/auditorium/channel/1524/2/playlist.m3u8";
        PlaylistData playlistData2 = new PlaylistData.Builder()
                .withPath(url2)
                .withStreamInfo(new StreamInfo.Builder().withCodecs(Arrays.asList("mp4a.40.2,avc1.77.31")).withBandwidth(1444864).withResolution(new Resolution(1280, 720)).withClosedCaptions("NONE").withAudio("audiogroup").build()).withUri(url2)
                .build();

        String url3 = "https://host/playlist/auditorium/channel/1524/7/playlist.m3u8";
        PlaylistData playlistData3 = new PlaylistData.Builder()
                .withPath(url3)
                .withStreamInfo(new StreamInfo.Builder().withCodecs(Arrays.asList("mp4a.40.2,avc1.77.30")).withBandwidth(252928).withResolution(new Resolution(640, 360)).withClosedCaptions("NONE").withAudio("audiogroup").build()).withUri(url3)
                .build();

        String url4 = "https://host/playlist/auditorium/channel/1524/5/playlist.m3u8";
        PlaylistData playlistData4 = new PlaylistData.Builder()
                .withPath(url4)
                .withStreamInfo(new StreamInfo.Builder().withCodecs(Arrays.asList("mp4a.40.2,avc1.77.21")).withBandwidth(172032).withResolution(new Resolution(448, 252)).withClosedCaptions("NONE").withAudio("audiogroup").build()).withUri(url4)
                .build();

        MediaData mediaData1 = new MediaData.Builder().withLanguage("en").withAutoSelect(true).withForced(false).withType(MediaType.AUDIO).withUri("uri1").withGroupId("audiogroup").withDefault(true).withName("English (US)").build();
        MediaData mediaData2 = new MediaData.Builder().withLanguage("en").withAutoSelect(true).withForced(false).withType(MediaType.AUDIO).withUri("uri2").withGroupId("audiogroup").withDefault(true).withName("English (US)").build();

        MasterPlaylist masterPlaylist = new MasterPlaylist.Builder().withPlaylists(Arrays.asList(playlistData1, playlistData2, playlistData3, playlistData4)).withMediaData(Arrays.asList(mediaData1, mediaData2)).build();

        Playlist playlist = new Playlist.Builder().withCompatibilityVersion(6).withMasterPlaylist(masterPlaylist).build();

        // when
        extendedM3uWriter.write(playlist);

        // then
        String generatedPlaylist = outputStream.toString(Encoding.UTF_8.value);

        // TODO: there is no independent header in the playlist

        String expectedPlaylist = "#EXTM3U\n" +
                "#EXT-X-VERSION:6\n" +
                "#EXT-X-MEDIA:LANGUAGE=\"en\",AUTOSELECT=YES,FORCED=NO,TYPE=AUDIO,URI=\"uri1\",GROUP-ID=\"audiogroup\",DEFAULT=YES,NAME=\"English (US)\"\n" +
                "#EXT-X-MEDIA:LANGUAGE=\"en\",AUTOSELECT=YES,FORCED=NO,TYPE=AUDIO,URI=\"uri2\",GROUP-ID=\"audiogroup\",DEFAULT=YES,NAME=\"English (US)\"\n" +
                "#EXT-X-ALLOW-CACHE:NO\n" +
                "#EXT-X-STREAM-INF:CODECS=\"mp4a.40.2,avc1.77.31\",RESOLUTION=864x486,BANDWIDTH=356352,AUDIO=\"audiogroup\",CLOSED-CAPTIONS=\"NONE\"\n" +
                "https://host/playlist/auditorium/channel/1524/4/playlist.m3u8\n" +
                "#EXT-X-STREAM-INF:CODECS=\"mp4a.40.2,avc1.77.31\",RESOLUTION=1280x720,BANDWIDTH=1444864,AUDIO=\"audiogroup\",CLOSED-CAPTIONS=\"NONE\"\n" +
                "https://host/playlist/auditorium/channel/1524/2/playlist.m3u8\n" +
                "#EXT-X-STREAM-INF:CODECS=\"mp4a.40.2,avc1.77.30\",RESOLUTION=640x360,BANDWIDTH=252928,AUDIO=\"audiogroup\",CLOSED-CAPTIONS=\"NONE\"\n" +
                "https://host/playlist/auditorium/channel/1524/7/playlist.m3u8\n" +
                "#EXT-X-STREAM-INF:CODECS=\"mp4a.40.2,avc1.77.21\",RESOLUTION=448x252,BANDWIDTH=172032,AUDIO=\"audiogroup\",CLOSED-CAPTIONS=\"NONE\"\n" +
                "https://host/playlist/auditorium/channel/1524/5/playlist.m3u8\n";

        assertThat("Invalid generated playlist", generatedPlaylist, is(expectedPlaylist));
    }
}