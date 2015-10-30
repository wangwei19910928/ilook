import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaViewer;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.ToolFactory;


public class DecodeAndPlayAudioAndVideo extends MediaListenerAdapter
{
  /**
   * Takes a media container (file) as the first argument, opens it,
   * plays audio as quickly as it can, and opens up a Swing window and
   * displays video frames with <i>roughly</i> the right timing.
   *  
   * @param args Must contain one string which represents a filename
   */

  public static void main(String[] args)
  {
    IMediaReader reader = ToolFactory.makeReader("E:/eclipse-jee-juno-SR1-win32/eclipse-jee-juno-SR1-win32/workspace/ILook/target/test.mp4");
      
      // create a new reader


      //
      // Create a MediaViewer object and tell it to play video only
      //
      reader.addListener(ToolFactory.makeViewer(IMediaViewer.Mode.VIDEO_ONLY));

      // read out the contents of the media file, and sit back and watch

      while (reader.readPacket() == null)
        do {} while(false);
      
    }
    
  }

