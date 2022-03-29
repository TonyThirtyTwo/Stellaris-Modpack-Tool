# Stellaris Modpack Tool
 A tool to simplify the creation of modpacks for the video-game Stellaris

TESTED ON WINDOWS ONLY

# How to use
Download https://github.com/TonyThirtyTwo/Stellaris-Modpack-Tool/raw/main/ModpackTool.jar

In the Stellaris launcher, go to 'All installed mods' and create a mod which will be the basis for your modpack. Then, have a playlist with all the mods you want to have in a modpack (load order is still important here, for the modpack to work your playlist should also work). You will want to export the json file for your playlist. To do this, go to the Playsets tab, select your playlist, click on the 3 dots in the top right, click on share, then download and save the json file anywhere on your computer.

Open the ModpackTool.jar file.

Select workshop mod folder. If Stellaris is in installed in the default Steam directory, then this should already be written for you, just press select, then open. The directory should end with \workshop\content\281990

Select the output folder. This will be the folder that you named your created mod after. For example, if you named your mod test123, then your directory should end in something like \Documents\Paradox Interactive\Stellaris\mod\test123

Click on 'Get from JSON' and select the json file that you had saved. If done correctly, you will see the mod ids in the text area below.

Click Start to begin the process, the status will display the progress of mods being transferred.

Done! Now you can create a new playset with the new big mod you've created. You can also go into the mods directory and zip the modpack to send to your friends (as it was created for).

# Credits
json-simple redistributed in this github according to the license
